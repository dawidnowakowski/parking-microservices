package com.dn.parking.reservationservice.repository;

import com.dn.parking.reservationservice.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.reservationId != :reservationId " +
            "AND r.parkingSpotId = :spotId " +
            "AND r.startDate < :newEnd " +
            "AND r.endDate > :newStart " +
            "AND r.status <> 'CANCELLED'")
    boolean existsOverlappingReservation(String reservationId, String spotId, LocalDateTime newStart, LocalDateTime newEnd);
}
