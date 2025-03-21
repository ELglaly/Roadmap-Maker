package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@ResponseStatus(INTERNAL_SERVER_ERROR)
public class RoadmapApiException extends AppException {
    public RoadmapApiException(HttpStatusCode statusCode, Throwable cause) {
        super("API request failed with "+cause.getMessage(), INTERNAL_SERVER_ERROR);
    }
}
