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

    private boolean notNumber(String countryCode) {
        return !countryCode.matches("^\\+[0-9]+$");
    }

    private boolean valueNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    }