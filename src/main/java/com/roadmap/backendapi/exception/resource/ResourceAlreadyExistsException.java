package com.roadmap.backendapi.exception.resource;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource already exists.
 * This exception is used to indicate that the resource being added or updated
 * already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.Resource
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends AppException {
    public ResourceAlreadyExistsException() {

      super("RESOURCE_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
