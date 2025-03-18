package com.roadmap.backendapi.exception.address;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AddressNotFoundException extends AppException {
    public AddressNotFoundException() {

      super("ADDRESS_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
