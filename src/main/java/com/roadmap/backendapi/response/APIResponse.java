package com.roadmap.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * APIResponse class represents a standard response structure for API responses.
 * It contains a message and data object to be returned in the response.
 */
@Setter
@Getter
@AllArgsConstructor
public class    APIResponse {
    // Getters and Setters
    private String message;
    private Object data;
}
