package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the Roadmap AI Service is not available.
 * This exception is used to indicate that the Roadmap AI Service is not available
 * and the request cannot be processed.
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoadmapAIServiceException extends AppException {

    public RoadmapAIServiceException() {
        super("Roadmap AI Service is not available at",HttpStatus.BAD_REQUEST);
    }
}
