package com.dn.parking.reservationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReservationDTO implements Serializable {
    @NotBlank(message = "missing email")
    @Email(message = "email is not properly formatted")
    private String email;

    @NotNull(message = "startDate is missing")
    private LocalDateTime startDate;

    @NotNull(message = "endDate is missing")
    private LocalDateTime endDate;

    @NotBlank(message = "parkingSpotId is missing")
    private String parkingSpotId;

    @NotBlank(message = "cardNumber")
    private String cardNumber;
}
