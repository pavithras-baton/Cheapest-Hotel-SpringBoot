package com.example.hotel.exception;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "400 BAD REQUEST");
        errorResponse.put("error", "Invalid Input Format");
        errorResponse.put("message", ex.getMessage()); 
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleDateTimeParse(DateTimeParseException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "400 BAD REQUEST");
        errorResponse.put("error", "Invalid Date Format");
        errorResponse.put("message", "Dates must strictly follow the 'dMMMyyyy' format (e.g., 1Dec2025).");
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
}

