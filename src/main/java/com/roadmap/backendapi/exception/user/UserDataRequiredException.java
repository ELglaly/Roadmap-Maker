package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserDataRequiredException extends AppException {
    public UserDataRequiredException() {

        super("User details (goal, interests, skills) are required for Generating Roadmap", HttpStatus.NOT_FOUND);
    }
}
