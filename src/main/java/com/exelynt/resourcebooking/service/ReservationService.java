package com.exelynt.resourcebooking.service;

import com.exelynt.resourcebooking.dto.reservation.ReservationRequest;
import com.exelynt.resourcebooking.dto.reservation.ReservationResponse;
import com.exelynt.resourcebooking.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ReservationService {

    ReservationResponse createReservation(
            ReservationRequest request,
            String userEmail
    );

    Page<ReservationResponse> getReservations(
            String userEmail,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    ReservationResponse getReservationById(
            Long id,
            String userEmail
    );

    ReservationResponse updateReservation(
            Long id,
            ReservationRequest request
    );

    void deleteReservation(Long id);
}