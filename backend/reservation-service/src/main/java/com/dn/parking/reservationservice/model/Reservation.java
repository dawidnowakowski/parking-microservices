package com.dn.parking.reservationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    private String reservationId;

    private String email;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String parkingSpotId;
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Version
    private Long version;
}
