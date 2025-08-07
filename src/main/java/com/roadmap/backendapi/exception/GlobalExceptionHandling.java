package com.roadmap.backendapi.exception;

import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/*
 * Global exception handling for the application.
 * This class handles exceptions thrown by the application
 * and returns a standardized error response.
 * @see com.roadmap.backendapi.response.APIErrorResponse
 * @see org.springframework.http.ResponseEntity
 */
@ControllerAdvice
public class GlobalExceptionHandling {


    /**
     * Handles AppException and returns a standardized error response.
     *
     * @param ex The AppException thrown by the application.
     * @return A ResponseEntity containing the error details and HTTP status.
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<List<ErrorResponse>> handleGlobalException(AppException ex) {

        if(ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            return new ResponseEntity<>(ex.getErrors(), ex.getStatus());
        } else {
            ErrorResponse errorResponse = new ErrorResponse("error", ex.getMessage());
            return new ResponseEntity<>(List.of(errorResponse), ex.getStatus());
        }
    }

    /**
     * Handles all other exceptions and returns a standardized error response.
     *
     * @param ex The exception thrown by the application.
     * @return A ResponseEntity containing the error details and HTTP status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ErrorResponse>> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("error", ex.getMessage());
        return new ResponseEntity<>(List.of(errorResponse), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
