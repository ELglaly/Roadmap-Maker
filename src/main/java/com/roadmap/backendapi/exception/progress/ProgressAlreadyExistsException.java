package com.roadmap.backendapi.exception.progress;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a progress is not found.
 * This exception is used to indicate that the progress being searched for
 * does not exist in the system.
 *
 * @see com.roadmap.backendapi.entity.Progress
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ProgressAlreadyExistsException extends AppException {
    public ProgressAlreadyExistsException() {

      super("PROGRESS_NOT_FOUND",HttpStatus.CONFLICT);
    }
}
