package com.roadmap.backendapi.exception;

import com.roadmap.backendapi.response.APIErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<APIErrorResponse>> handleGlobalException(AppException ex) {

        if(ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            return new ResponseEntity<>(ex.getErrors(), ex.getStatus());
        } else {
            APIErrorResponse apiErrorResponse = new APIErrorResponse("error", ex.getMessage());
            return new ResponseEntity<>(List.of(apiErrorResponse), ex.getStatus());
        }

    }
}
