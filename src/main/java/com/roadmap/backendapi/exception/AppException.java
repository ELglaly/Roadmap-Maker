package com.roadmap.backendapi.exception;

import com.roadmap.backendapi.response.APIErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom exception class for handling application-specific exceptions.
 * This class extends RuntimeException and includes additional fields
 * for HTTP status and error messages.
 *
 * @see org.springframework.http.HttpStatus
 * @see APIErrorResponse
 */
@Getter
@Setter
@AllArgsConstructor
public class AppException extends RuntimeException {
    private final HttpStatus status;
    private List<APIErrorResponse> errors = new ArrayList<>();

    // Constructor for creating an AppException with a message and HTTP status
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Constructor for creating an AppException with a list of ErrorResponse objects and HTTP status
    public AppException(List<APIErrorResponse> errors, HttpStatus status) {
        this.errors = errors;
        this.status = status;
    }

}
