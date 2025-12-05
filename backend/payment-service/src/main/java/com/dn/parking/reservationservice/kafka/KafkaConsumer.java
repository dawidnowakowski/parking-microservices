package com.dn.parking.reservationservice.kafka;


import com.dn.parking.reservationservice.dto.ReservationDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final String TOPIC_NAME = "reservation-requests";

    @KafkaListener(topics = "reservation-requests")
    public void processMessage(ReservationDTO reservationDTO) {
        System.out.println("Received message: " + reservationDTO);
    }
}
