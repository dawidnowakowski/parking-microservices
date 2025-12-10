package com.dn.parking.apigateway.controller;

import com.dn.parking.apigateway.exception.InvalidReservationDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidReservationDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidReservationDateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
