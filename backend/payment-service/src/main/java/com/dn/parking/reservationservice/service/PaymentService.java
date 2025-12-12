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

    public Double calculateAmount(LocalDateTime startDate, LocalDateTime endDate) {
        long millis = Duration.between(startDate, endDate).toMillis();
        double hours = Math.ceil(millis / 3_600_000.0);
        return Math.max(6.0, hours * 6.0);
    }
}
