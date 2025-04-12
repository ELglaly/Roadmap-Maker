package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is an error generating a token.
 * This exception is used to indicate that the token generation process
 * has failed and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenGenerationException extends AppException {
    public TokenGenerationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
