package com.iot_station_management.iot_station_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

// class that handles exceptions thrown and returns corresponding HTTP statuses
@ControllerAdvice
public class IotDeviceEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentExceptions(Exception ex, WebRequest request) {
        IotExceptionDetails errorDetails = new IotExceptionDetails(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<Object> handleNoSuchElementExceptions(Exception ex, WebRequest request) {
        IotExceptionDetails errorDetails = new IotExceptionDetails(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleInvalidMethodArgumentException(Exception ex, WebRequest request) {
        IotExceptionDetails errorDetails = new IotExceptionDetails(LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
