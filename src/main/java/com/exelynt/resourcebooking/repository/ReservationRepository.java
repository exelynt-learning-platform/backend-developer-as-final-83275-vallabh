package com.exelynt.resourcebooking.repository;

import com.exelynt.resourcebooking.entity.Reservation;
import com.exelynt.resourcebooking.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE (:email IS NULL OR r.user.email = :email)
              AND (:status IS NULL OR r.status = :status)
              AND (:minPrice IS NULL OR r.price >= :minPrice)
              AND (:maxPrice IS NULL OR r.price <= :maxPrice)
            """)
    Page<Reservation> findReservations(
            @Param("email") String email,
            @Param("status") ReservationStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}