package com.roadmap.backendapi.exception.milestone;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MilestoneNotFoundException extends AppException {
    public MilestoneNotFoundException() {

      super("MILESTONE_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
