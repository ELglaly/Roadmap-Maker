package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user already exists.
 * This exception is used to indicate that the user
 * being created already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends AppException {
    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
