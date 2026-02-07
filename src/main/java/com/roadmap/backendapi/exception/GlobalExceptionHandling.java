package com.roadmap.backendapi.exception;

import com.roadmap.backendapi.response.APIErrorResponse;
import com.roadmap.backendapi.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handling for the application.
 * This class handles exceptions thrown by the application
 * and returns standardized error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandling {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandling.class);

    /**
     * Handles validation errors (Bean Validation).
     *
     * @param ex The MethodArgumentNotValidException containing validation errors
     * @param request The HTTP request
     * @return A ResponseEntity with structured validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<APIErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> APIErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .code(error.getCode())
                        .build())
                .collect(Collectors.toList());

        APIErrorResponse response = APIErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed for one or more fields")
                .path(request.getRequestURI())
                .withCorrelationId()
                .fieldErrors(fieldErrors)
                .build();

        log.warn("Validation error on path {}: {} field(s) failed",
                request.getRequestURI(), fieldErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles type mismatch errors (e.g., passing string when number expected).
     *
     * @param ex The MethodArgumentTypeMismatchException
     * @param request The HTTP request
     * @return A ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = String.format("Parameter '%s' must be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        APIErrorResponse response = APIErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .withCorrelationId()
                .build();

        log.warn("Type mismatch error on path {}: {}", request.getRequestURI(), message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex The IllegalArgumentException
     * @param request The HTTP request
     * @return A ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        APIErrorResponse response = APIErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .withCorrelationId()
                .build();

        log.warn("Illegal argument on path {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles AppException (custom application exceptions).
     *
     * @param ex The AppException thrown by the application
     * @param request The HTTP request
     * @return A ResponseEntity containing the error details and HTTP status
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIErrorResponse> handleAppException(
            AppException ex,
            HttpServletRequest request) {

        // Convert old ErrorResponse list to field errors if present
        List<APIErrorResponse.FieldError> fieldErrors = null;
        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            fieldErrors = ex.getErrors().stream()
                    .map(error -> APIErrorResponse.FieldError.builder()
                            .field(error.getErrorField())
                            .message(error.getErrorMessage())
                            .build())
                    .collect(Collectors.toList());
        }

        APIErrorResponse response = APIErrorResponse.builder()
                .timestamp(Instant.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .withCorrelationId()
                .fieldErrors(fieldErrors)
                .build();

        log.warn("Application exception on path {}: {} - {}",
                request.getRequestURI(), ex.getStatus(), ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    /**
     * Handles all uncaught exceptions as a fallback.
     *
     * @param ex The exception thrown by the application
     * @param request The HTTP request
     * @return A ResponseEntity containing generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        APIErrorResponse response = APIErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .withCorrelationId()
                .exception(ex.getClass().getSimpleName()) // Only for debugging
                .build();

        log.error("Unhandled exception on path {}: {}",
                request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
