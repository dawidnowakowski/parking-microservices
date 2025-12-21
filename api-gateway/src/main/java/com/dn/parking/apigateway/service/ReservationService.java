package com.dn.parking.apigateway.service;

import com.dn.parking.apigateway.dto.ReservationRequest;
import com.dn.parking.apigateway.exception.InvalidReservationDateException;
import com.dn.parking.apigateway.exception.NotFoundException;
import com.dn.parking.apigateway.model.Reservation;
import com.dn.parking.apigateway.model.ReservationStatus;
import com.dn.parking.apigateway.repository.ReservationRepository;
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
    private final KafkaTemplate<String, ReservationRequest> kafkaTemplate;
    private final ReservationRepository reservationRepository;

    public String createReservation(ReservationRequest reservationDTO) {
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

    public Reservation getReservation(String key) throws NotFoundException {
        return reservationRepository.findById(key)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + key));
    }

    public void handleSuccessMessage(String key, String source, String message) {
        Reservation reservation = reservationRepository.findById(key).isPresent() ? reservationRepository.findById(key).get() : null; // should always be found
        assert reservation != null;
        if (reservation.getStatus().equals(ReservationStatus.PENDING)) {
            if (source.equals("RESERVATION")) reservation.setStatus(ReservationStatus.CONFIRMED_RESERVATION);
            if (source.equals("PAYMENT")) reservation.setStatus(ReservationStatus.CONFIRMED_PAYMENT);
            reservation.setUpdateMessages(message);
            reservationRepository.save(reservation);
        } else if (reservation.getStatus().equals(ReservationStatus.CONFIRMED_RESERVATION) || reservation.getStatus().equals(ReservationStatus.CONFIRMED_PAYMENT)) {
            reservation.setStatus(ReservationStatus.CONFIRMED_ALL);
            reservation.setUpdateMessages(String.join(";", reservation.getUpdateMessages(), message));
            reservationRepository.save(reservation);
        }


    }

    public void handleErrorMessage(String key, String message) {
        Reservation reservation = reservationRepository.findById(key).isPresent() ? reservationRepository.findById(key).get() : null; // should always be found
        assert reservation != null;

        reservation.setStatus(ReservationStatus.CANCELLED);
        if (reservation.getErrorMessages() == null) {
            reservation.setErrorMessages(message);
        } else {
            reservation.setErrorMessages(String.join(";", reservation.getErrorMessages(), message));
        }
        reservationRepository.save(reservation);
    }
}
