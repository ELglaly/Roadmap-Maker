package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an invalid token is provided.
 * This exception is used to indicate that the token provided
 * is not valid and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends AppException {
    public InvalidTokenException(String message)
    {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
