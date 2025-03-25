package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
        return RegistrationRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user= (User) target;
        ValidationUtils.invokeValidator(validateUserCommon, user, errors);
        if(user.getPassword()==null)
        {
            errors.rejectValue("password", "field.required", "Password is required.");
        }
        else {
            ValidationUtils.invokeValidator(passwordValidator, user.getPassword(), errors);
        }

    }


}