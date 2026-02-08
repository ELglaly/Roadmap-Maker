package com.roadmap.backendapi.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a prompt template is not found.
 */
public class PromptTemplateNotFoundException extends AppException {

    /**
     * Creates a new PromptTemplateNotFoundException.
     *
     * @param message the error message
     */
    public PromptTemplateNotFoundException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
