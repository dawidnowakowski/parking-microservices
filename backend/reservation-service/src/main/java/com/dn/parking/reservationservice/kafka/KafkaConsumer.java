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
    private static final String TOPIC_REQUESTS = "reservation-requests";
    private final String TOPIC_SAGA = "saga-alert";
    private final ReservationService reservationService;
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = TOPIC_REQUESTS)
    public void processMessage(
            @Payload Reservation message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Reservation request received from Kafka topic: {} with key: {}", TOPIC_REQUESTS, key);
        message.setReservationId(key);
        message.setStatus(ReservationStatus.RESERVED);
        reservationService.save(message);
    }

    @KafkaListener(topics = TOPIC_SAGA)
    public void processSagaMessage(
            @Header("source") String source,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        System.out.println(String.join(source, key));
        reservationService.handleSaga(source, key);
    }
}
