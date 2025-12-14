package com.dn.parking.paymentservice.model;

import jakarta.persistence.*;
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

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Double amount;

    private Integer cvv;

    @Version
    private Long version;
}
