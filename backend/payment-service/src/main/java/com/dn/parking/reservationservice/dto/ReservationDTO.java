package com.dn.parking.reservationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private String uuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String cardNumber;
}
