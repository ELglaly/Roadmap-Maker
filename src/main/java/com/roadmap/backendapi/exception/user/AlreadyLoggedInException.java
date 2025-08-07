package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is already logged in.
 * This exception is used to indicate that the user is already logged in
 * and cannot perform the requested action.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyLoggedInException extends AppException {
    public AlreadyLoggedInException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
