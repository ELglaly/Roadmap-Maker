package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoadMapNotFoundException extends AppException {
    public RoadMapNotFoundException() {

      super("RESOURCE_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
