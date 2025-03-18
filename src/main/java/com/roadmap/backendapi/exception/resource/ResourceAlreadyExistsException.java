package com.roadmap.backendapi.exception.resource;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends AppException {
    public ResourceAlreadyExistsException() {

      super("RESOURCE_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
