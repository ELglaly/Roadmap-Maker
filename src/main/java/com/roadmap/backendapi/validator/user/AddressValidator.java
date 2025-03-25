package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Address.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Address address = (Address) target;

        // Add more specific validation for address fields as needed
        if (valueNullOrEmpty(address.getZip()) && !address.getZip().matches("\\d{5}(-\\d{4})?")) {
            errors.rejectValue("zipCode", "field.invalid", "Invalid zip code format.");
        }
        if (valueNullOrEmpty(address.getCity()) && notString(address.getCity())) {
            errors.rejectValue("city", "field.invalid", "Invalid city name format.");
        }
        if (valueNullOrEmpty(address.getStreet()) && notString(address.getStreet())) {
            errors.rejectValue("state", "field.invalid", "Invalid state name format.");
        }
        if (valueNullOrEmpty(address.getCountry()) && notString(address.getCountry())) {
            errors.rejectValue("country", "field.invalid", "Invalid country name format.");
        }
    }

    private boolean notString(String city) {
        return !city.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
    }

    private boolean valueNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    }