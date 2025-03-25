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


        private boolean isValidEmail (String email){
            // Basic email validation regex (you can use a more robust one)
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            return email.matches(emailRegex);
        }
    }