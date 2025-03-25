package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz); // Validate the password string directly
    }

    @Override
    public void validate(Object target, Errors errors) {
        String password = (String) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required", "Password is required.");
        if (!password.isEmpty() && password.length() < 8) {
            errors.rejectValue("password", "field.minlength", "Password must be at least 8 characters long.");
        }
        else if (!password.isEmpty() && !isValidPassword(password)) {
            errors.rejectValue("password", "field.constrains", "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace");
        }
    }

    private boolean isValidPassword(String password) {
        String emailRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(emailRegex);
    }
}