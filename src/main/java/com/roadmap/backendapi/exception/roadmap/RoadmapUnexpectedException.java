package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when an unexpected error occurs while generating a roadmap.
 * This exception is used to indicate that an unexpected error occurred
 * and the request cannot be processed.
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoadmapUnexpectedException extends AppException {
    public RoadmapUnexpectedException() {
        super("Unexpected error while generating roadmap, Try Again Later",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}