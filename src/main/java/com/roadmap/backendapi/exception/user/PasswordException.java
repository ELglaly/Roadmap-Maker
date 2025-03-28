package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.response.ErrorResponse;
import com.roadmap.backendapi.utils.ErrorHandling;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordException extends AppException {
    public PasswordException(String message)
    {
        super(message, HttpStatus.BAD_REQUEST);
    }
   public PasswordException(Errors errors)
    {
      super(ErrorHandling.getErrorMessages(errors), HttpStatus.BAD_REQUEST);
    }
}
