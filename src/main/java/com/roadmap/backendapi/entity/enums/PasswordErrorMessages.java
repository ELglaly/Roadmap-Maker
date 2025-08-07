package com.roadmap.backendapi.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PasswordErrorMessages {
    PASSWORD_TOO_SHORT("Password must be at least 8 characters long"),
    PASSWORD_REQUIRED("Password is required"),
    PASSWORD_MISMATCH("Passwords do not match"),
    PASSWORD_INCORRECT("Password is incorrect"),
    PASSWORD_CONSTRAINTS("Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace");

    private final String message;

}
