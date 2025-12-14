package com.dn.parking.apigateway.kafka;


import com.dn.parking.apigateway.service.ReservationService;
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
    private final String TOPIC_STATUS = "status-updates";
    private final ReservationService reservationService;
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = TOPIC_STATUS)
    public void processMessage(
            @Payload String message,
            @Header("event-type") String eventType,
            @Header("source") String source,
            @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info("Status {} received from Kafka topic: {} with key: {}", eventType, TOPIC_STATUS, key);
        if (eventType.equals("0")) {
            reservationService.handleSuccessMessage(key, source, message);
        } else {
            reservationService.handleErrorMessage(key, message);
        }
    }
}
