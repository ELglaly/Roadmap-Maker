package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends AppException {
    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
