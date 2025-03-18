package com.roadmap.backendapi.exception;

import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
public class AppException extends RuntimeException {
    private HttpStatus status;
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
