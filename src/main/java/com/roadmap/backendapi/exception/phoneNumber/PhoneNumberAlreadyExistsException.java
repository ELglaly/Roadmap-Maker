package com.roadmap.backendapi.exception.phoneNumber;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneNumberAlreadyExistsException extends AppException {
    public PhoneNumberAlreadyExistsException() {

      super("PHONE_NUMBER_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
