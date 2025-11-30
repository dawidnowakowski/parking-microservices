package com.dn.parking.apigateway.service;

import com.dn.parking.apigateway.dto.ReservationDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationService {
    public String createReservation(ReservationDTO reservationDTO) {
        String uuid = UUID.randomUUID().toString();
        //
        return uuid;
    }
}
