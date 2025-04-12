package com.roadmap.backendapi.exception.address;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an address is not found.
 * This exception is used to indicate that the address being searched for
 * does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.Address
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AddressNotFoundException extends AppException {
    public AddressNotFoundException() {

      super("ADDRESS_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
