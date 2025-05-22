package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * AddressValidator is a Spring Validator that validates the fields of an Address object.
 * It checks if the fields are not null or empty and if they match specific formats.
 */
@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Address.class.equals(clazz);
    }

    /**
     * Validates the Address object.
     *
     * @param target the object to validate
     * @param errors the Errors object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        Address address = (Address) target;

        // Add more specific validation for address fields as needed
        if (!valueNullOrEmpty(address.getZip()) && !address.getZip().matches("\\d{5}(-\\d{4})?")) {
            errors.rejectValue("zipCode", "field.invalid", "Invalid zip code format.");
        }
        if (!valueNullOrEmpty(address.getCity()) && notString(address.getCity())) {
            errors.rejectValue("city", "field.invalid", "Invalid city name format.");
        }
        if (!valueNullOrEmpty(address.getStreet()) && notString(address.getStreet())) {
            errors.rejectValue("state", "field.invalid", "Invalid state name format.");
        }
        if (!valueNullOrEmpty(address.getCountry()) && notString(address.getCountry())) {
            errors.rejectValue("country", "field.invalid", "Invalid country name format.");
        }
    }

    /**
     * Checks if a city name is not a valid string.
     *
     * @param city the city name to check
     * @return true if the city name is not valid, false otherwise
     */
    private boolean notString(String city) {
        return !city.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
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