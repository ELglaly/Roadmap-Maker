package com.roadmap.backendapi.exception.progress;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProgressPercentageException extends AppException {
    public ProgressPercentageException() {

        super("Progress percentage must be between 0 and 100",HttpStatus.BAD_REQUEST);
    }
}
