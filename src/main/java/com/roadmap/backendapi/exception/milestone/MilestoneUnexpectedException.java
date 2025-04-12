package com.roadmap.backendapi.exception.milestone;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an unexpected error occurs while processing a milestone.
 * This exception is used to indicate that an unexpected error has occurred
 * while processing a milestone.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MilestoneUnexpectedException extends AppException {
    public MilestoneUnexpectedException(String message) {
        super(message+ "Try to regenerate The roadmap Again", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}