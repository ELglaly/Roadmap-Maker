package com.roadmap.backendapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ErrorHandling class provides utility methods for handling errors in the application.
 * It includes methods to convert error messages into a standard format.
 */
public class ErrorHandling {

    /**
     * Converts a list of error messages into a standard format.
     *
     * @param errors The Errors object containing validation errors.
     * @return A list of ErrorResponse objects representing the error messages.
     */
    public static List<ErrorResponse> getErrorMessages(Errors errors) {
       List<ErrorResponse> errorResponses =new ArrayList<>();
        if (errors.hasErrors()) {
            // Process field errors

            errors.getFieldErrors().forEach(fieldError -> {
               ErrorResponse errorResponse= new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage());
                errorResponses.add(errorResponse);
            });
        }
       return errorResponses;
    }
}
