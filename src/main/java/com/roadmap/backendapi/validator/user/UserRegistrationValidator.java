package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * UserRegistrationValidator is a Spring Validator that validates the fields of a User object during registration.
 * It checks if the fields are not null or empty and if they match specific formats.
 */
@Component
public class UserRegistrationValidator implements Validator {
    private final PasswordValidator passwordValidator;
    private final ValidateUserCommon validateUserCommon;

    public UserRegistrationValidator( PasswordValidator passwordValidator1, ValidateUserCommon validateUserCommon) {
        this.passwordValidator = passwordValidator1;
        this.validateUserCommon = validateUserCommon;
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
        User user= (User) target;
        ValidationUtils.invokeValidator(validateUserCommon, user, errors);
        if(user.getUserSecurity().getPasswordHash() ==null)
        {
            errors.rejectValue("password", "field.required", "Password is required.");
        }
        else {
            ValidationUtils.invokeValidator(passwordValidator, user.getUserSecurity().getPasswordHash(), errors);
        }

    }


}