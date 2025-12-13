package com.dn.parking.apigateway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

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

    @NotNull(message = "registrationNumber is missing")
    @Size(message = "registrationNumber must be 4 to 8 characters long")
    private String registrationNumber;

    @NotBlank(message = "parkingSpotId is missing")
    private String parkingSpotId;

    @NotBlank(message = "cardNumber is empty")
    private String cardNumber;

    @NotNull(message = "cvv is missing")
    @Range(message = "cvv must be in range 100-999", min = 100, max = 999)
    private Integer cvv;
}
