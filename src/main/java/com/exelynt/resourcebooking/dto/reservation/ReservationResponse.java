package com.exelynt.resourcebooking.dto.reservation;

import com.exelynt.resourcebooking.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long userId,
        Long resourceId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal price,
        ReservationStatus status
) {
}