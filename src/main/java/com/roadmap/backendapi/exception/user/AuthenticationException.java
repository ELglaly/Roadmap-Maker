package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user is not authenticated.
 * This exception is used to indicate that the user is not authenticated
 * and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends AppException {
    public AuthenticationException(String message)
    {
        super(message,HttpStatus.UNAUTHORIZED);
    }
}
