package com.roadmap.backendapi.exception.user;
import com.roadmap.backendapi.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Exception thrown when a user fails to authenticate.
 * This exception is used to indicate that the authentication attempt
 * was unsuccessful and the request cannot be processed.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
public class unsuccessfulAuthentication extends AppException {
    public unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException missingOrInvalidJwtToken) {
        super(missingOrInvalidJwtToken.getMessage(), missingOrInvalidJwtToken.getStatus());
    }
}
