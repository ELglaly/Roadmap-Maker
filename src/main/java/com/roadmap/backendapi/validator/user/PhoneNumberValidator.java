package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.PhoneNumber;
import com.roadmap.backendapi.exception.phoneNumber.PhoneNumberAlreadyExistsException;
import com.roadmap.backendapi.repository.PhoneNumberRepository;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.response.ErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * PhoneNumberValidator is a Spring Validator that validates phone numbers.
 * It checks if the phone number is not empty, has a valid format, and does not already exist in the database.
 */
@Component
public class PhoneNumberValidator implements Validator {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberValidator(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PhoneNumber.class.equals(clazz);
    }

    /**
     * Validates the PhoneNumber object.
     *
     * @param target the object to validate
     * @param errors the Errors object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        PhoneNumber phoneNumber = (PhoneNumber) target;


        if(valueNullOrEmpty(phoneNumber.getCountryCode()) && notNumber(phoneNumber.getCountryCode())){
            errors.rejectValue("countryCode", "field.invalid", "Invalid country code format.");
        }

        // Add more specific validation for phone number fields as needed
        if (valueNullOrEmpty(phoneNumber.getNumber())  && !phoneNumber.getNumber().matches("\\d{10}")) {
            errors.rejectValue("number", "field.invalid", "Invalid phone number format.");
        }
        if (phoneNumberRepository.existsByNumber(phoneNumber.getNumber())) {
            ErrorResponse errorResponse =new ErrorResponse("number", "Phone number already exists");
            throw new PhoneNumberAlreadyExistsException(List.of(errorResponse));

        }
    }

    /**
     * Checks if a country code is not a valid number.
     *
     * @param countryCode the country code to check
     * @return true if the country code is not valid, false otherwise
     */
    private boolean notNumber(String countryCode) {
        return !countryCode.matches("^\\+[0-9]+$");
    }

    /**
     * Checks if a value is null or empty.
     *
     * @param value the value to check
     * @return true if the value is null or empty, false otherwise
     */
    private boolean valueNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    }