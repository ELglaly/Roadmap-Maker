package com.roadmap.backendapi.exception.progress;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProgressNotFoundException extends AppException {
    public ProgressNotFoundException() {

      super("PROGRESS_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
