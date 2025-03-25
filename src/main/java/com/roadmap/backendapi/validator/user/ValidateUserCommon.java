package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required", "First name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required", "Last name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required", "Username is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "goal", "field.required", "Goal is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "interests", "field.required", "Interests are required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "skills", "field.required", "Skills are required.");

        if (user.getEmail() == null) {
            errors.rejectValue("email", "field.required", "Email is required.");
        } else {
            ValidationUtils.invokeValidator(emailValidator, user.getEmail(), errors);
        }

        if ( valueNullOrEmpty(user.getFirstName()) && (user.getFirstName().length() < 2 || user.getFirstName().length() > 50)) {
            errors.rejectValue("firstName", "field.size", "First name must be between 2 and 50 characters.");
        }
        if (valueNullOrEmpty(user.getFirstName()) && (user.getLastName().length() < 2 || user.getLastName().length() > 50)) {
            errors.rejectValue("lastName", "field.size", "Last name must be between 2 and 50 characters.");
        }
        if (user.getUsername() != null && (user.getUsername().length() < 5 || user.getUsername().length() > 20)) {
            errors.rejectValue("username", "field.size", "Username must be between 5 and 20 characters.");
        } else if (userRepository.existsByUsername(user.getUsername())) {
            errors.rejectValue("username", "field.duplicate", "Username already exists.");
        }
        if (valueNullOrEmpty(user.getFirstName()) && user.getGoal().length() > 500) {
            errors.rejectValue("goal", "field.maxlength", "Goal cannot exceed 500 characters.");
        }
        if (valueNullOrEmpty(user.getFirstName()) && user.getInterests().length() > 500) {
            errors.rejectValue("interests", "field.maxlength", "Interests cannot exceed 500 characters.");
        }
        if (valueNullOrEmpty(user.getFirstName()) && user.getSkills().length() > 500) {
            errors.rejectValue("skills", "field.maxlength", "Skills cannot exceed 500 characters.");
        }
    }

    private boolean valueNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
