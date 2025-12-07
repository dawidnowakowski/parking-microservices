package com.dn.parking.reservationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private String uuid;
    private String email;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String cardNumber;
}
