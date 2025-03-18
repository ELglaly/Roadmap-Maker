package com.roadmap.backendapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(AppException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }
}
