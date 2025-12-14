package com.dn.parking.apigateway.controller;

import com.dn.parking.apigateway.dto.ReservationRequest;
import com.dn.parking.apigateway.model.Reservation;
import com.dn.parking.apigateway.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody @Valid ReservationRequest reservationDto) {
        String reservationId = reservationService.createReservation(reservationDto);
        return ResponseEntity.accepted().body(reservationId);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String id) {
        Reservation reservation = reservationService.getReservation(id);
        return ResponseEntity.ok(reservation);
    }
}
