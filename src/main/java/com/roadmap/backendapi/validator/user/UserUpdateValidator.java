package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import org.springframework.validation.annotation.Validated;
/**
 * UserUpdateValidator is a Spring Validator that validates the fields of a User object during update operations.
 * It checks if the fields are not null or empty and if they match specific formats.
 */
@Component
public class UserUpdateValidator implements Validator {
    private final PhoneNumberValidator phoneNumberValidator;
    private final AddressValidator addressValidator;
    private final ValidateUserCommon validateUserCommon;
    private final UserMapper userMapper;

    public UserUpdateValidator(PhoneNumberValidator phoneNumberValidator, AddressValidator addressValidator, ValidateUserCommon validateUserCommon, UserMapper userMapper) {
        this.phoneNumberValidator = phoneNumberValidator;
        this.addressValidator = addressValidator;
        this.validateUserCommon = validateUserCommon;
        this.userMapper = userMapper;
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
        User updateUserRequest = (User) target;

        ValidationUtils.invokeValidator(validateUserCommon, updateUserRequest, errors);

        if (updateUserRequest.getPhoneNumber() != null) {
            BindingResult phoneErrors = new BeanPropertyBindingResult(updateUserRequest.getPhoneNumber(), errors.getObjectName());
            ValidationUtils.invokeValidator(phoneNumberValidator, updateUserRequest.getPhoneNumber(), phoneErrors);
            if (phoneErrors.hasErrors()) {
                errors.addAllErrors(phoneErrors);
            }
        }

        if (updateUserRequest.getAddress() != null) {
            BindingResult addressErrors = new BeanPropertyBindingResult(updateUserRequest.getAddress(), errors.getObjectName());
            ValidationUtils.invokeValidator(addressValidator, updateUserRequest.getAddress(), addressErrors);
            if (addressErrors.hasErrors()) {
                errors.addAllErrors(addressErrors);
            }
        }
    }
}