package com.dn.parking.paymentservice.kafka;


import com.dn.parking.paymentservice.dto.ReservationDTO;
import com.dn.parking.paymentservice.model.Payment;
import com.dn.parking.paymentservice.model.PaymentStatus;
import com.dn.parking.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private static final String TOPIC_NAME = "reservation-requests";
    private final String TOPIC_SAGA = "saga-alert";
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final PaymentService paymentService;

    @KafkaListener(topics = TOPIC_NAME)
    public void processMessage(
            @Payload ReservationDTO message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_NAME, key);
        Payment payment = new Payment();
        payment.setPaymentId(key);
        payment.setCardNumber(message.getCardNumber());
        payment.setCvv(message.getCvv());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(paymentService.calculateAmount(message.getStartDate(), message.getEndDate()));

        paymentService.sendPaymentRequest(payment);

    }

    @KafkaListener(topics = TOPIC_SAGA)
    public void processSagaMessage(
            @Header("source") String source,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Received Saga for key: {}", key);
        paymentService.handleSaga(source, key);
    }
}
