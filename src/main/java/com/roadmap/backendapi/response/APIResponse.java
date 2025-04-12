package com.roadmap.backendapi.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * APIResponse class represents a standard response structure for API responses.
 * It contains a message and data object to be returned in the response.
 */
@Setter
@Getter
public class    APIResponse {
    // Getters and Setters
    private String message;
    private Object data;

    // Constructor
    public APIResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
