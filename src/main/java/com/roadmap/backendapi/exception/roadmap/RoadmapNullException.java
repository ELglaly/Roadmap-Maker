package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the generated roadmap is null.
 * This exception is used to indicate that the generated roadmap is null
 * and the request cannot be processed.
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoadmapNullException extends AppException {
    public RoadmapNullException() {

      super("Generated roadmap is null",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
