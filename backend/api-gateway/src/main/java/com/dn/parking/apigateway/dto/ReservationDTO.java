package com.dn.parking.apigateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    @NotBlank(message = "missing email")
    @Email(message = "email is not properly formatted")
    private String email;

    @Future(message = "startDate must be in the future")
    @NotNull(message = "startDate is missing")
    private LocalDateTime startDate;

    @Future(message = "endDate must be in the future")
    @NotNull(message = "endDate is missing")
    private LocalDateTime endDate;

    @NotBlank(message = "parkingSpotId is missing")
    private String parkingSpotId;

    @NotBlank(message = "cardNumber")
    private String cardNumber;
}
