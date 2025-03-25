package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.utils.ErrorHandling;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserValidationException extends AppException {
    public UserValidationException(Errors error) {
        super(ErrorHandling.getErrorMessages(error), HttpStatus.BAD_REQUEST);
    }
}
