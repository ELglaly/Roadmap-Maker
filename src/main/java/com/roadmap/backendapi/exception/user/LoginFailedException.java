package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginFailedException extends AppException {
    public LoginFailedException(String message)
    {
        super(message,HttpStatus.BAD_REQUEST);
    }
}
