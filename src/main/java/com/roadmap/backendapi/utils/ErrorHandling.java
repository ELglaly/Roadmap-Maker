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

public class ErrorHandling {

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
