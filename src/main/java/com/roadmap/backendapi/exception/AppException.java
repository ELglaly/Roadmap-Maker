package com.roadmap.backendapi.exception;

import com.google.api.client.json.Json;
import com.roadmap.backendapi.response.ErrorResponse;
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
 * @see com.roadmap.backendapi.response.ErrorResponse
 */
@Getter
@Setter
public class AppException extends RuntimeException {
    private HttpStatus status;
    List<ErrorResponse> errors = new ArrayList<>();

    // Constructor for creating an AppException with a message and HTTP status
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    // Constructor for creating an AppException with a list of ErrorResponse objects and HTTP status
    public AppException(List<ErrorResponse> errors, HttpStatus status) {
        this.errors = errors;
        this.status = status;
    }

}
