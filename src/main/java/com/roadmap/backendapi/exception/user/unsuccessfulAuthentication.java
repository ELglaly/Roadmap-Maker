package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
public class unsuccessfulAuthentication extends AppException {
    public unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException missingOrInvalidJwtToken) {
        super(missingOrInvalidJwtToken.getMessage(), missingOrInvalidJwtToken.getStatus());
    }
}
