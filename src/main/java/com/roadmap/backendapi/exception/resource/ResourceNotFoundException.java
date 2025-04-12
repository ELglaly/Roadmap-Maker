package com.roadmap.backendapi.exception.resource;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource is not found.
 * This exception is used to indicate that the resource being searched for
 * does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.Resource
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException() {

      super("RESOURCE_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
