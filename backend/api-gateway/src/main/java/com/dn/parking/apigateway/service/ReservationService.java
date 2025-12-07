package com.dn.parking.apigateway.service;

import com.dn.parking.apigateway.dto.ReservationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationService {
    private static final String TOPIC_NAME = "reservation-requests";
    Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final KafkaTemplate<String, ReservationDTO> kafkaTemplate;

    public ReservationService(KafkaTemplate<String, ReservationDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String createReservation(ReservationDTO reservationDTO) {
        String uuid = UUID.randomUUID().toString();
        kafkaTemplate.send(TOPIC_NAME, uuid, reservationDTO);
        logger.info("Reservation request sent to Kafka topic: {} with uuid: {}", TOPIC_NAME, uuid);
        return uuid;
    }
}
