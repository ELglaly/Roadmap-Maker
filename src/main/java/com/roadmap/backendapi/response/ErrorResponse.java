package com.roadmap.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * ErrorResponse class represents a standard error response structure for API errors.
 * It contains a field name and an error message to be returned in the response.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String fieldName;
    private String message;
}
