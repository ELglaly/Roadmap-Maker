package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoadMapAlreadyExistsException extends AppException {
    public RoadMapAlreadyExistsException() {

      super("RoadMap_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
