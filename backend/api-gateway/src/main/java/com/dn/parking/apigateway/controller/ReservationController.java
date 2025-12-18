package com.dn.parking.apigateway.controller;

import com.dn.parking.apigateway.dto.ReservationRequest;
import com.dn.parking.apigateway.model.Reservation;
import com.dn.parking.apigateway.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Fetch reservation status",
            description = "Fetches reservation status if reservation exists",
            responses = {
                    @ApiResponse(
                            responseCode ="200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Reservation.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class))
                    )
            }
    )
    public ResponseEntity<Reservation> getReservation(@PathVariable String id) {
        Reservation reservation = reservationService.getReservation(id);
        return ResponseEntity.ok(reservation);
    }
}
