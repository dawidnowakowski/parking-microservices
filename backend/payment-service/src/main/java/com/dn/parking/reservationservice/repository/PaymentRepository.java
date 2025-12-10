package com.dn.parking.reservationservice.repository;

import com.dn.parking.reservationservice.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {
}
