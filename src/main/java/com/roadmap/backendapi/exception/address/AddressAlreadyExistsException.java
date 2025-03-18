package com.roadmap.backendapi.exception.address;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AddressAlreadyExistsException extends AppException {
    public AddressAlreadyExistsException() {

      super("ADDRESS_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
