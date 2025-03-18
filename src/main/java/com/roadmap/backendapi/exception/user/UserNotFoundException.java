package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends AppException {
    public UserNotFoundException() {

        super("USER_NOT_FOUND" , HttpStatus.NOT_FOUND);
    }
}
