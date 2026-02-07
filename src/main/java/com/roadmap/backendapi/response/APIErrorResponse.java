package com.roadmap.backendapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.List;

/**
 * Standardized error response for all API errors.
 * Provides comprehensive error information including timestamp, status, path, and optional field errors.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    private final Instant timestamp;

    /**
     * HTTP status code (e.g., 400, 404, 500)
     */
    private final int status;

    /**
     * HTTP status reason phrase (e.g., "Bad Request", "Not Found")
     */
    private final String error;

    /**
     * Human-readable error message
     */
    private final String message;

    /**
     * Request path that caused the error
     */
    private final String path;

    /**
     * Correlation ID for request tracing (from MDC if available)
     */
    private final String correlationId;

    /**
     * Detailed field-level validation errors (for 400 Bad Request)
     */
    private final List<FieldError> fieldErrors;

    /**
     * Exception class name (only in development mode)
     */
    private final String exception;

    /**
     * Represents a single field validation error
     */
    @Getter
    @Builder
    public static class FieldError {
        /**
         * Name of the field that failed validation
         */
        private final String field;

        /**
         * Validation error message
         */
        private final String message;

        /**
         * The rejected value that caused the error
         */
        private final Object rejectedValue;

        /**
         * Validation constraint that was violated (e.g., "NotNull", "Size")
         */
        private final String code;
    }

    /**
     * Builder class to simplify APIErrorResponse creation
     */
    public static class APIErrorResponseBuilder {
        // Lombok generates the builder, but we can add helper methods

        /**
         * Automatically sets timestamp to now
         */
        public APIErrorResponseBuilder withCurrentTimestamp() {
            this.timestamp = Instant.now();
            return this;
        }

        /**
         * Automatically extracts correlation ID from MDC
         */
        public APIErrorResponseBuilder withCorrelationId() {
            this.correlationId = MDC.get("correlationId");
            return this;
        }
    }
}
