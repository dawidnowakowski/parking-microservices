package com.dn.parking.reservationservice.service;

import com.dn.parking.reservationservice.model.Reservation;
import com.dn.parking.reservationservice.model.ReservationStatus;
import com.dn.parking.reservationservice.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final String TOPIC_STATUS = "status-updates";
    private final String TOPIC_SAGA = "saga-alert";

    Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void save(Reservation reservation) {
        boolean isTaken = reservationRepository.existsOverlappingReservation(
                reservation.getReservationId(),
                reservation.getParkingSpotId(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );

        if (isTaken) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            try {
                reservationRepository.save(reservation);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            logger.info("ReservationId: {} cancelled - spot is taken", reservation.getReservationId());

            Message<String> errorMsg = MessageBuilder
                    .withPayload("Parking spot already taken")
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_STATUS)
                    .setHeader(KafkaHeaders.KEY, reservation.getReservationId())
                    .setHeader("source", "RESERVATION")
                    .setHeader("event-type", "1")
                    .build();
            kafkaTemplate.send(errorMsg);

            Message<String> sagaMsg = MessageBuilder
                    .withPayload("")
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_SAGA)
                    .setHeader(KafkaHeaders.KEY, reservation.getReservationId())
                    .setHeader("source", "RESERVATION")
                    .build();
            kafkaTemplate.send(sagaMsg);
        } else {
            reservation.setStatus(ReservationStatus.RESERVED);
            try {
                reservationRepository.save(reservation);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            logger.info("ReservationId: {} successful - spot is now reserved", reservation.getReservationId());

            Message<String> successMsg = MessageBuilder
                    .withPayload("Successfully reserved parking spot")
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_STATUS)
                    .setHeader(KafkaHeaders.KEY, reservation.getReservationId())
                    .setHeader("source", "RESERVATION")
                    .setHeader("event-type", "0")
                    .build();
            kafkaTemplate.send(successMsg);
        }
    }

    @Transactional
    public void handleSaga(String source, String key) {
        if (!source.equals("RESERVATION")) { // if source is the same as target then record is already cancelled. If not then it may be that record was either created or not
            reservationRepository.findById(key).ifPresentOrElse(
                    reservation -> reservation.setStatus(ReservationStatus.CANCELLED),
                    () -> {
                        Reservation reservation = new Reservation();
                        reservation.setReservationId(key);
                        reservation.setStatus(ReservationStatus.CANCELLED);
                        try {
                            reservationRepository.save(reservation);
                        } catch (Exception e) {
                            handleSaga(source, key); // should only repeat once if save method execution has ended in the meanwhile
                        }
                    }
            );
        }

    }
}
