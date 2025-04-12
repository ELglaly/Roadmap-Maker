package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Exception thrown when an error occurs while processing a Roadmap API request.
 * This exception is used to indicate that an error has occurred while processing
 * a Roadmap API request.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 */

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class RoadmapApiException extends AppException {
    public RoadmapApiException(HttpStatusCode statusCode, Throwable cause) {
        super("API request failed with "+cause.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
