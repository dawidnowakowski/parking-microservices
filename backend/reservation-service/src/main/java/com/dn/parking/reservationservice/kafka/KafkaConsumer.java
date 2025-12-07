package com.dn.parking.reservationservice.kafka;


import com.dn.parking.reservationservice.dto.ReservationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final String TOPIC_NAME = "reservation-requests";
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = TOPIC_NAME)
    public void processMessage(
            @Payload ReservationDTO message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        message.setUuid(key);
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_NAME, key);
    }
}
