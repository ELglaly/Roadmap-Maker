package com.roadmap.backendapi.exception.phoneNumber;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Exception thrown when a phone number already exists.
 * This exception is used to indicate that the phone number being added or updated
 * already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.PhoneNumber
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneNumberAlreadyExistsException extends AppException {
    public PhoneNumberAlreadyExistsException() {
      super("PHONE_NUMBER_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
    public PhoneNumberAlreadyExistsException(List<ErrorResponse> errorResponse) {
        super(errorResponse, HttpStatus.CONFLICT);
    }
}
