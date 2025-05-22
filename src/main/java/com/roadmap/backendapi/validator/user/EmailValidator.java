package com.roadmap.backendapi.validator.user;

import com.roadmap.backendapi.exception.user.EmailAlreadyExistsException;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.response.ErrorResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

/**
 * EmailValidator is a Spring Validator that validates email addresses.
 * It checks if the email is not empty, has a valid format, and does not already exist in the database.
 */
@Component
public class EmailValidator implements Validator {

    private final UserRepository userRepository;

    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz); // Validate the email string directly
    }

    /**
     * Validates the email address.
     *
     * @param target the email address to validate
     * @param errors the Errors object to hold validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
            String email = (String) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "Email is required.");

            if (!email.isEmpty() && !isValidEmail(email)) {
                errors.rejectValue("email", "field.invalid", "Invalid email format.");
            }
            else if (!email.isEmpty() && userRepository.existsByEmail(email)) {
                ErrorResponse errorResponse = new ErrorResponse("email", "Email already exists");
                throw new EmailAlreadyExistsException(List.of(errorResponse));
            }
        }


        /**
         * Checks if the email address is valid.
         *
         * @param email the email address to check
         * @return true if the email address is valid, false otherwise
         */
// Import java.util.regex.Pattern for email validation
private boolean isValidEmail(String email) {
    // Use a more robust email regex pattern
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return Pattern.compile(emailRegex).matcher(email).matches();
}
    }