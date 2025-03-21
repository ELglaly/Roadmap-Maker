package com.roadmap.backendapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConnectionErrorException extends AppException {
    public ConnectionErrorException() {

        super("Your Connection Is Unstable, Try Again Later", HttpStatus.BAD_REQUEST);
    }
}
