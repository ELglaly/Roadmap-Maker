package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.utils.ErrorHandling;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user tries to register with an email that already exists.
 * This exception is used to indicate that the email is already in use
 * and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordException extends AppException {
    public PasswordException(String message)
    {
        super(message, HttpStatus.BAD_REQUEST);
    }
   public PasswordException(Errors errors)
    {
      super(ErrorHandling.getAPIErrorResponses(errors), HttpStatus.BAD_REQUEST);
    }
}
