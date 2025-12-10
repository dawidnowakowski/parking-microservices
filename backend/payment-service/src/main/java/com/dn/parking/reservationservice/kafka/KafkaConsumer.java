package com.dn.parking.reservationservice.kafka;


import com.dn.parking.reservationservice.dto.ReservationDTO;
import com.dn.parking.reservationservice.model.Payment;
import com.dn.parking.reservationservice.model.PaymentStatus;
import com.dn.parking.reservationservice.service.PaymentService;
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
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final PaymentService paymentService;

    @KafkaListener(topics = TOPIC_NAME)
    public void processMessage(
            @Payload ReservationDTO message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_NAME, key);
        Payment payment = new Payment();
        payment.setPaymentId(key);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(paymentService.calculateAmount(message.getStartDate(), message.getEndDate()));
        paymentService.save(payment);

        // todo communicate with SOAP
    }
}
