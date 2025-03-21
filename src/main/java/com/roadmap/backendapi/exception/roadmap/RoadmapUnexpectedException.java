package com.roadmap.backendapi.exception.roadmap;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoadmapUnexpectedException extends AppException {
    public RoadmapUnexpectedException() {
        super("Unexpected error while generating roadmap, Try Again Later",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}