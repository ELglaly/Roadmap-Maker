package com.roadmap.backendapi.utils;

import com.roadmap.backendapi.response.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

/**
 * ErrorHandling class provides utility methods for handling errors in the application.
 * It includes methods to convert error messages into a standard format.
 */
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ErrorHandling {

    /**
     * Converts a list of error messages into a standard format.
     *
     * @param errors The Errors object containing validation errors.
     * @return A list of ErrorResponse objects representing the error messages.
     */
    public static List<ErrorResponse> getAPIErrorResponses(Errors errors) {
       List<ErrorResponse> errorRespons =new ArrayList<>();
        if (errors.hasErrors()) {
            // Process field errors
            errors.getFieldErrors().forEach(fieldError -> {
               ErrorResponse errorResponse = new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
                errorRespons.add(errorResponse);
            });
        }
       return errorRespons;
    }
}
