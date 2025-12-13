package com.dn.parking.paymentservice.kafka;


import com.dn.parking.paymentservice.dto.ReservationDTO;
import com.dn.parking.paymentservice.model.Payment;
import com.dn.parking.paymentservice.model.PaymentStatus;
import com.dn.parking.paymentservice.service.PaymentService;
import lab.paymentsoapservice.PaymentFault_Exception;
import lab.paymentsoapservice.PaymentRequest;
import lab.paymentsoapservice.PaymentResponse;
import lab.paymentsoapservice.PaymentService_Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private static final String TOPIC_NAME = "reservation-requests";
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final PaymentService paymentService;
    private static final QName SERVICE_NAME = new QName("http://paymentsoapservice.lab/", "PaymentService");

    @KafkaListener(topics = TOPIC_NAME)
    public void processMessage(
            @Payload ReservationDTO message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_NAME, key);
        Payment payment = new Payment();
        payment.setPaymentId(key);
        payment.setCardNumber(message.getCardNumber());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(paymentService.calculateAmount(message.getStartDate(), message.getEndDate()));


        URL wsdlURL = PaymentService_Service.WSDL_LOCATION;

        PaymentService_Service ss = new PaymentService_Service(wsdlURL, SERVICE_NAME);
        lab.paymentsoapservice.PaymentService port = ss.getPaymentServicePort();

        PaymentRequest soapRequest = new PaymentRequest();
        soapRequest.setAmount(payment.getAmount());
        soapRequest.setCardNumber(payment.getCardNumber());
        soapRequest.setCvv("123");

        try {
            PaymentResponse soapResponse = port.processPayment(soapRequest);
            if (soapResponse.isApproved()) {
                payment.setStatus(PaymentStatus.CONFIRMED);
                logger.info("Payment Approved for ID: {}", key);
            }
        } catch (PaymentFault_Exception e) {
            logger.error("SOAP Service returned a fault: {}", e.getFaultInfo().getMessage());
            payment.setStatus(PaymentStatus.REJECTED);
        }

        paymentService.save(payment);
    }
}
