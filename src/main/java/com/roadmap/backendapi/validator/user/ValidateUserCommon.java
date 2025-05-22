package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * ValidateUserCommon is a Spring Validator that validates the common fields of a User object.
 * It checks if the fields are not null or empty and if they match specific formats.
 */
@Component
public class ValidateUserCommon implements Validator {

    private final EmailValidator emailValidator;
    private final UserRepository userRepository;

    public ValidateUserCommon(EmailValidator emailValidator, UserRepository userRepository) {
        this.emailValidator = emailValidator;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Validates the User object.
     *
     * @param target the object to validate
     * @param errors the Errors object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        validateRequiredFields(user, errors);
        validateEmail(user, errors);
        validateNameFields(user, errors);
        validateUsername(user, errors);
        validateTextFields(user, errors);
    }

    private void validateRequiredFields(User user, Errors errors) {
        String[] fields = {"firstName", "lastName", "username", "goal", "interests", "skills"};
        for (String field : fields) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "field.required", field + " is required.");
        }
    }

    private void validateEmail(User user, Errors errors) {
        if (valueNullOrEmpty(user.getEmail())) {
            errors.rejectValue("email", "field.required", "Email is required.");
        } else {
            ValidationUtils.invokeValidator(emailValidator, user.getEmail(), errors);
        }
    }

    private void validateNameFields(User user, Errors errors) {
        validateFieldLength(user.getFirstName(), "firstName", 2, 50, errors);
        validateFieldLength(user.getLastName(), "lastName", 2, 50, errors);
    }

    private void validateUsername(User user, Errors errors) {
        if (valueNullOrEmpty(user.getUsername())) {
            validateFieldLength(user.getUsername(), "username", 5, 20, errors);
            if (userRepository.existsByUsername(user.getUsername())) {
                errors.rejectValue("username", "field.duplicate", "Username already exists.");
            }
        }
    }

    private void validateTextFields(User user, Errors errors) {
        validateFieldLength(user.getGoal(), "goal", 5, 500, errors);
        validateFieldLength(user.getInterests(), "interests", 4, 500, errors);
        validateFieldLength(user.getSkills(), "skills", 5, 500, errors);
    }

    private void validateFieldLength(String value, String fieldName, int minLength, int maxLength, Errors errors) {
        if (value != null && (value.length() < minLength || value.length() > maxLength)) {
            errors.rejectValue(fieldName, "field.size", fieldName + " must be between " + minLength + " and " + maxLength + " characters.");
        }
    }


    /**
     * Checks if the value is null or empty.
     *
     * @param value the value to check
     * @return true if the value is null or empty, false otherwise
     */
    private boolean valueNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
