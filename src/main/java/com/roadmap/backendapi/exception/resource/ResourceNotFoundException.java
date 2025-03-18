package com.roadmap.backendapi.exception.resource;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException() {

      super("RESOURCE_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
