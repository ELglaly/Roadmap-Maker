package com.roadmap.backendapi.exception.address;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an address already exists.
 * This exception is used to indicate that the address being added or updated
 * already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.Address
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AddressAlreadyExistsException extends AppException {

    public AddressAlreadyExistsException() {

      super("ADDRESS_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
