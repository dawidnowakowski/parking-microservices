package com.dn.parking.reservationservice.service;

import com.dn.parking.reservationservice.model.Reservation;
import com.dn.parking.reservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public String findById(String id) {
        return reservationRepository.findById(id)
                .map(reservation -> reservation.getStatus().name())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found for id: " + id
                ));
    }
}
