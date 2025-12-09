package com.dn.parking.reservationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Payment {
    @Id
    private String paymentId;

    private Long amount;
    private PaymentStatus status;
}
