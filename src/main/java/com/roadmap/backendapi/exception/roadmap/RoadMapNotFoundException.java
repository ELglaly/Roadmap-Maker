package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a RoadMap is not found.
 * This exception is used to indicate that the RoadMap being searched for
 * does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoadMapNotFoundException extends AppException {
    public RoadMapNotFoundException() {

      super("RESOURCE_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
