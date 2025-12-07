package com.dn.parking.reservationservice.kafka;


import com.dn.parking.reservationservice.model.Reservation;
import com.dn.parking.reservationservice.model.ReservationStatus;
import com.dn.parking.reservationservice.service.ReservationService;
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
    private final ReservationService reservationService;
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = TOPIC_NAME)
    public void processMessage(
            @Payload Reservation message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_NAME, key);
        message.setReservationId(key);
        message.setStatus(ReservationStatus.PENDING);
        reservationService.save(message);
    }
}
