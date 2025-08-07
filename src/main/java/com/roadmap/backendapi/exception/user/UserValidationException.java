package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.utils.ErrorHandling;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when user validation fails.
 * This exception is used to indicate that the user data provided
 * does not meet the required validation criteria.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserValidationException extends AppException {
    public UserValidationException(Errors error) {
        super(ErrorHandling.getAPIErrorResponses(error), HttpStatus.BAD_REQUEST);
    }
}
