package com.roadmap.backendapi.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
