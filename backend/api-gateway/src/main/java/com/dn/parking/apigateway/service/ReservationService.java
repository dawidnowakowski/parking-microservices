package com.dn.parking.apigateway.service;

import com.dn.parking.apigateway.dto.ReservationDTO;
import com.dn.parking.apigateway.exception.InvalidReservationDateException;
import com.dn.parking.apigateway.model.Reservation;
import com.dn.parking.apigateway.model.ReservationStatus;
import com.dn.parking.apigateway.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final String TOPIC_NAME = "reservation-requests";
    Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final KafkaTemplate<String, ReservationDTO> kafkaTemplate;
    private final ReservationRepository reservationRepository;

    @Transactional
    public String createReservation(ReservationDTO reservationDTO) {
        if (!reservationDTO.getEndDate().isAfter(reservationDTO.getStartDate())) {
            throw new InvalidReservationDateException("Start date must be before the end date");
        }

        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation entity = reservationRepository.save(reservation);
        String reservationId = entity.getId();
        kafkaTemplate.send(TOPIC_NAME, reservationId, reservationDTO);
        logger.info("Reservation request sent to Kafka topic: {} with uuid: {}", TOPIC_NAME, reservationId);
        return reservationId;
    }
}
