package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when a user is not found.
 * This exception is used to indicate that the user
 * being searched for does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends AppException {
    public UserNotFoundException() {

        super("USER_NOT_FOUND" , HttpStatus.NOT_FOUND);
    }
}
