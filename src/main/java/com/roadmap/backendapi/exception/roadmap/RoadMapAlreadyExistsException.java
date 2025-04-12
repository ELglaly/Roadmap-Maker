package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a RoadMap already exists.
 * This exception is used to indicate that the RoadMap being created
 * already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class RoadMapAlreadyExistsException extends AppException {
    public RoadMapAlreadyExistsException() {

      super("RoadMap_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
