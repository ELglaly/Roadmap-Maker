package com.roadmap.backendapi.exception.milestone;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a milestone is not found.
 * This exception is used to indicate that the milestone being searched for
 * does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MilestoneNotFoundException extends AppException {
    public MilestoneNotFoundException() {

      super("MILESTONE_NOT_FOUND",HttpStatus.NOT_FOUND);
    }
}
