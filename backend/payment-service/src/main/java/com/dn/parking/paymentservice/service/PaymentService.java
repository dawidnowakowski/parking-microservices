package com.dn.parking.paymentservice.service;

import com.dn.parking.paymentservice.model.Payment;
import com.dn.parking.paymentservice.model.PaymentStatus;
import com.dn.parking.paymentservice.repository.PaymentRepository;
import lab.paymentsoapservice.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final String TOPIC_STATUS = "status-updates";
    private final String TOPIC_SAGA = "saga-alert";
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final QName SERVICE_NAME = new QName("http://paymentsoapservice.lab/", "PaymentService");
    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public Double calculateAmount(LocalDateTime startDate, LocalDateTime endDate) {
        long millis = Duration.between(startDate, endDate).toMillis();
        double hours = Math.ceil(millis / 3_600_000.0);
        return Math.max(6.0, hours * 6.0);
    }

    public void sendPaymentRequest(Payment payment) {
        URL wsdlURL = PaymentService_Service.WSDL_LOCATION;

        PaymentService_Service ss = new PaymentService_Service(wsdlURL, SERVICE_NAME);
        lab.paymentsoapservice.PaymentService port = ss.getPaymentServicePort();

        PaymentRequest soapRequest = new PaymentRequest();
        soapRequest.setAmount(payment.getAmount());
        soapRequest.setCardNumber(payment.getCardNumber());
        soapRequest.setCvv(payment.getCvv().toString());

        // save with PENDING status
        if (paymentRepository.findById(payment.getPaymentId()).isPresent()) {
            logger.info("Payment request already exists for key: {}", payment.getPaymentId());
            return;
        } else {
            paymentRepository.save(payment);
            logger.info("Payment request saved for key: {}", payment.getPaymentId());
        }

        try {
            PaymentResponse soapResponse = port.processPayment(soapRequest);
            if (soapResponse.isApproved()) {
                logger.info("Payment Approved for ID: {}", payment.getPaymentId());
                payment.setStatus(PaymentStatus.CONFIRMED);
                try {
                    paymentRepository.save(payment);
                    logger.info("Record created for key: {}", payment.getPaymentId());
                } catch (Exception e) {
                    logger.error("Record was canceled during SOAP request for key: {}. Sending refund request", payment.getPaymentId());
                    sendRefundRequest(payment);
                    return;
                }

                Message<String> successMsg = MessageBuilder
                        .withPayload("Payment approved")
                        .setHeader(KafkaHeaders.TOPIC, TOPIC_STATUS)
                        .setHeader(KafkaHeaders.KEY, payment.getPaymentId())
                        .setHeader("source", "PAYMENT")
                        .setHeader("event-type", "0")
                        .build();
                kafkaTemplate.send(successMsg);
            } // else = catch
        } catch (PaymentFault_Exception e) {
            logger.error("SOAP Service returned a fault: {}", e.getFaultInfo().getMessage());
            payment.setStatus(PaymentStatus.CANCELLED);
            try {
                logger.info("Creating a record (canceled) for key: {}", payment.getPaymentId());
                paymentRepository.save(payment);
            } catch (Exception ex) {
                logger.error("Record was canceled during SOAP request for key: {}", payment.getPaymentId());
            }

            Message<String> errorMsg = MessageBuilder
                    .withPayload("Payment rejected by bank")
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_STATUS)
                    .setHeader(KafkaHeaders.KEY, payment.getPaymentId())
                    .setHeader("source", "PAYMENT")
                    .setHeader("event-type", "1")
                    .build();
            kafkaTemplate.send(errorMsg);

            Message<String> sagaMsg = MessageBuilder
                    .withPayload("")
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_SAGA)
                    .setHeader(KafkaHeaders.KEY, payment.getPaymentId())
                    .setHeader("source", "PAYMENT")
                    .build();
            kafkaTemplate.send(sagaMsg);
        }
    }

    public void handleSaga(String source, String key) {
        if (!source.equals("PAYMENT")) {
            Optional<Payment> paymentOpt = paymentRepository.findById(key);

            if (paymentOpt.isPresent()) {
                logger.info("Payment request already exists for key: {}", key);
                if (paymentOpt.get().getStatus().equals(PaymentStatus.CONFIRMED)) {
                    paymentOpt.get().setStatus(PaymentStatus.CANCELLED);
                    paymentRepository.save(paymentOpt.get());
                    sendRefundRequest(paymentOpt.get());
                } else if (paymentOpt.get().getStatus().equals(PaymentStatus.PENDING)) {
                    paymentOpt.get().setStatus(PaymentStatus.CANCELLED);
                    try {
                        paymentRepository.save(paymentOpt.get());
                    } catch (Exception ex) {
                        // in the meantime record was created
                        handleSaga(source, key);
                    }
                    // don't send refund request because it means that there is another thread waiting for soap response and it will call the method after receiving soap response
                }
                // another else-if would be CANCELLED but in this case we don't have to change anything
            } else {
                Payment payment = new Payment();
                payment.setPaymentId(key);
                payment.setStatus(PaymentStatus.CANCELLED);
                try {
                    paymentRepository.save(payment);
                    logger.info("Payment not yet registered - not sending refund for: {}", key);
                    // not sending refund because the payment hasn't started and when it starts it will see the canceled state adn return
                } catch (Exception ex) {
                    // version has changed -> record was created in the meantime by processing
                    handleSaga(source, key);
                }
            }
        }
    }

    public void sendRefundRequest(Payment payment) {
        logger.info("Sending Refund Request for transaction {}", payment.getPaymentId());
        URL wsdlURL = PaymentService_Service.WSDL_LOCATION;

        PaymentService_Service ss = new PaymentService_Service(wsdlURL, SERVICE_NAME);
        lab.paymentsoapservice.PaymentService port = ss.getPaymentServicePort();

        RefundRequest soapRequest = new RefundRequest();
        soapRequest.setTransactionId(payment.getPaymentId());

        try {
            RefundResponse soapResponse = port.processRefund(soapRequest);
            logger.info("Refund Response for transaction {}", payment.getPaymentId());
            if (soapResponse.isSuccess()) {
                paymentRepository.findById(payment.getPaymentId()).ifPresent(p -> {
                    p.setStatus(PaymentStatus.REFUNDED);
                    paymentRepository.save(p);
                });
            }
        } catch (Exception e) {
            paymentRepository.findById(payment.getPaymentId()).ifPresent(p -> {
                p.setStatus(PaymentStatus.REFUND_ERROR);
                paymentRepository.save(p);
            });
        }
    }
}
