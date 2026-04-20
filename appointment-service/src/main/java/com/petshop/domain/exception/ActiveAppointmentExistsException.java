package com.petshop.domain.exception;

public class ActiveAppointmentExistsException extends RuntimeException {
    public ActiveAppointmentExistsException(String message) {
        super(message);
    }
}
