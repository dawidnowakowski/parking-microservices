package com.dn.parking.reservationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Payment {
    @Id
    private String paymentId;

    private Double amount;
    private String status;
}
