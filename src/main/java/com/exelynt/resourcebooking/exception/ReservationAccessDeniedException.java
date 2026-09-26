package com.exelynt.resourcebooking.exception;

public class ReservationAccessDeniedException extends RuntimeException {

    public ReservationAccessDeniedException(String message) {
        super(message);
    }
}