package com.roadmap.backendapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is a connection error.
 * This exception is used to indicate that the connection
 * is unstable and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.user.User
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class    ConnectionErrorException extends AppException {
    public ConnectionErrorException() {
        super("Your Connection Is Unstable, Try Again Later", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
