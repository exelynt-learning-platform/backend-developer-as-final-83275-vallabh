package com.exelynt.resourcebooking.repository;

import com.exelynt.resourcebooking.entity.Reservation;
import com.exelynt.resourcebooking.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByUserEmail(
            String email,
            Pageable pageable
    );

    Page<Reservation> findByUserEmailAndStatus(
            String email,
            ReservationStatus status,
            Pageable pageable
    );

    Page<Reservation> findByStatus(
            ReservationStatus status,
            Pageable pageable
    );

    Page<Reservation> findByPriceGreaterThanEqual(
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Reservation> findByPriceLessThanEqual(
            BigDecimal maxPrice,
            Pageable pageable
    );
}