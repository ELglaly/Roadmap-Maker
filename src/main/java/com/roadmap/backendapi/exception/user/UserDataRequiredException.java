package com.roadmap.backendapi.exception.user;

import com.roadmap.backendapi.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when user data is required for generating a roadmap.
 * This exception is used to indicate that the user details (goal, interests, skills)
 * are required for generating a roadmap.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * * @see com.roadmap.backendapi.service.user.UserService
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserDataRequiredException extends AppException {
    public UserDataRequiredException() {

        super("User details (goal, interests, skills) are required for Generating Roadmap", HttpStatus.NOT_FOUND);
    }
}
