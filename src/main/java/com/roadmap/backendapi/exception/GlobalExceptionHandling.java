package com.roadmap.backendapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/*
 * Global exception handling for the application.
 * This class handles exceptions thrown by the application
 * and returns a standardized error response.
 *
 * @see com.roadmap.backendapi.exception.user.UserNotFoundException
 * @see com.roadmap.backendapi.exception.user.UserAlreadyExistsException
 * @see com.roadmap.backendapi.exception.user.UserDataRequiredException
 * @see com.roadmap.backendapi.exception.user.TokenGenerationException
 */
@ControllerAdvice
public class GlobalExceptionHandling {
    /**
     * Handles AppException and returns a standardized error response.
     *
     * @param ex the AppException to handle
     * @return a ResponseEntity containing the error details
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(AppException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }
}
