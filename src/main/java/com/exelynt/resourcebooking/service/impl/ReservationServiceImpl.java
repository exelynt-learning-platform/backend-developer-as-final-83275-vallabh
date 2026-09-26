package com.exelynt.resourcebooking.service.impl;

import com.exelynt.resourcebooking.dto.reservation.ReservationRequest;
import com.exelynt.resourcebooking.dto.reservation.ReservationResponse;
import com.exelynt.resourcebooking.entity.Reservation;
import com.exelynt.resourcebooking.entity.Resource;
import com.exelynt.resourcebooking.entity.User;
import com.exelynt.resourcebooking.enums.ReservationStatus;
import com.exelynt.resourcebooking.exception.ReservationAccessDeniedException;
import com.exelynt.resourcebooking.exception.ReservationNotFoundException;
import com.exelynt.resourcebooking.exception.ReservationValidationException;
import com.exelynt.resourcebooking.exception.ResourceNotFoundException;

import com.exelynt.resourcebooking.repository.ReservationRepository;
import com.exelynt.resourcebooking.repository.ResourceRepository;
import com.exelynt.resourcebooking.repository.UserRepository;
import com.exelynt.resourcebooking.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            ResourceRepository resourceRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional
    public ReservationResponse createReservation(
            ReservationRequest request,
            String userEmail
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ReservationValidationException(
                                "Authenticated user not found"
                        )
                );

        Resource resource = resourceRepository.findById(request.resourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + request.resourceId()
                        )
                );


        validateReservationTime(
                request.startTime(),
                request.endTime()
        );

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setPrice(request.price());
        reservation.setStatus(request.status());

        Reservation savedReservation = reservationRepository.save(reservation);

        return mapToResponse(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getReservations(
            String userEmail,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {

        Page<Reservation> reservations = reservationRepository.findReservations(
                userEmail,
                status,
                minPrice,
                maxPrice,
                pageable
        );

        return reservations.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(
            Long id,
            String userEmail
    ) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );

        if (userEmail != null &&
                !reservation.getUser().getEmail().equals(userEmail)) {

            throw new ReservationAccessDeniedException(
                    "You are not authorized to access this reservation"
            );
        }

        return mapToResponse(reservation);
    }

    @Override
    @Transactional
    public ReservationResponse updateReservation(
            Long id,
            ReservationRequest request
    ) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );

        Resource resource = resourceRepository.findById(request.resourceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + request.resourceId()
                        )
                );

        validateReservationTime(
                request.startTime(),
                request.endTime()
        );

        reservation.setResource(resource);
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setPrice(request.price());
        reservation.setStatus(request.status());

        Reservation updatedReservation =
                reservationRepository.save(reservation);

        return mapToResponse(updatedReservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );

        reservationRepository.delete(reservation);
    }

    private void validateReservationTime(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {

        if (!endTime.isAfter(startTime)) {
            throw new ReservationValidationException(
                    "End time must be after start time"
            );
        }
    }

    private ReservationResponse mapToResponse(
            Reservation reservation
    ) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getResource().getId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getStatus()
        );
    }
}