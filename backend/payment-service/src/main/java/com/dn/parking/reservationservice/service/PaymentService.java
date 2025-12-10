package com.dn.parking.reservationservice.service;

import com.dn.parking.reservationservice.model.Payment;
import com.dn.parking.reservationservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    final PaymentRepository paymentRepository;


    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public long calculateAmount(LocalDateTime startDate, LocalDateTime endDate) {
        return Math.round(Duration.between(startDate, endDate).toMinutes() / 60.0) * 6;
    }
}
