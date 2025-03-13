package com.roadmap.backendapi.exception.phoneNumber;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhoneNumberNotFoundException extends AppException {
    public PhoneNumberNotFoundException() {

      super("PHONE_NUMBER_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
