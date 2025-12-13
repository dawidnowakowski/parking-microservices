package com.dn.parking.paymentservice.repository;

import com.dn.parking.paymentservice.model.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {
}
