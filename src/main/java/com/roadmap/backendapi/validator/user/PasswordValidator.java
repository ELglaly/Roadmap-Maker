package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.utils.Const;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.roadmap.backendapi.utils.Const.PasswordErrorMessages.PASSWORD_REQUIRED;

/**
 * PasswordValidator is a Spring Validator that validates password strings.
 * It checks if the password is not empty, has a minimum length, and contains at least one digit,
 * one lowercase letter, one uppercase letter, one special character, and no whitespace.
 */
@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz); // Validate the password string directly
    }

    /**
     * Validates the password string.
     *
     * @param target the password string to validate
     * @param errors the Errors object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        String password = (String) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", PASSWORD_REQUIRED);
        if (!password.isEmpty() && password.length() < 8) {
            errors.rejectValue("password", "password.minlength", Const.PasswordErrorMessages.PASSWORD_TOO_SHORT);
        }
        else if (!password.isEmpty() && !isValidPassword(password)) {
            errors.rejectValue("password", "password.constrains", Const.PasswordErrorMessages.PASSWORD_CONSTRAINTS );
        }
    }


    /**
     * Checks if the password string is valid.
     *
     * @param password the password string to check
     * @return true if the password string is valid, false otherwise
     */
    private boolean isValidPassword(String password) {
        String emailRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(emailRegex);
    }
}