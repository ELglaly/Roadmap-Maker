package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Exception thrown when a user tries to register with an email that already exists.
 * This exception is used to indicate that the email is already in use
 * and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException(List<ErrorResponse> errors) {
        super(errors, HttpStatus.CONFLICT);
    }
}
