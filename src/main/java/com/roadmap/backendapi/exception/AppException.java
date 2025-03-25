package com.roadmap.backendapi.exception;

import com.google.api.client.json.Json;
import com.roadmap.backendapi.response.ErrorResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppException extends RuntimeException {
    private HttpStatus status;
    List<ErrorResponse> errors = new ArrayList<>();
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public AppException(List<ErrorResponse> errors, HttpStatus status) {
        this.errors = errors;
        this.status = status;
    }

}
