package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyLoggedInException extends AppException {
    public AlreadyLoggedInException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
