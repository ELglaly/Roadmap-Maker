package com.roadmap.backendapi.exception.milestone;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MilestoneAlreadyExistsException extends AppException {
    public MilestoneAlreadyExistsException() {

      super("MILESTONE_ALREADY_EXISTS",HttpStatus.CONFLICT);
    }
}
