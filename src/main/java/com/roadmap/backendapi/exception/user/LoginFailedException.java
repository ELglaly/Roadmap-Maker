package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user fails to log in.
 * This exception is used to indicate that the login attempt
 * was unsuccessful and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginFailedException extends AppException {
    public LoginFailedException(String message)
    {
        super(message,HttpStatus.BAD_REQUEST);
    }
}
