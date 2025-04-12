package com.roadmap.backendapi.exception.milestone;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a milestone already exists.
 * This exception is used to indicate that the milestone being added or updated
 * already exists in the system.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class MilestoneAlreadyExistsException extends AppException {
    public MilestoneAlreadyExistsException() {

      super("MILESTONE_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
