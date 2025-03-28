package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.Config.PasswordEncoder;
import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.exception.user.PasswordException;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.request.user.changePasswordRequest;
import com.roadmap.backendapi.response.ErrorResponse;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UseService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserUpdateValidator userUpdateValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserRegistrationValidator userRegistrationValidator,
                           UserUpdateValidator userUpdateValidator, PasswordEncoder passwordEncoder,
                           PasswordValidator passwordValidator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
        this.userUpdateValidator = userUpdateValidator;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }


    public UserDTO getUserById(Long userId) {
        User  user =userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);
        return userMapper.toDTO(user);
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {
        return "";
    }

    public UserDTO getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDTO getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDTO registerUser(RegistrationRequest registrationRequest)
    {
        User user= validateUser(registrationRequest);
        user.setPassword(passwordEncoder.passwordEncoder().encode(registrationRequest.getPassword()));
        user= userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public void changePassword(Long userId, changePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Errors errors = new BeanPropertyBindingResult(request.getNewPassword(), "newPassword");
        if (!Objects.equals(
                request.getNewPassword().trim(),
                request.getConfirmPassword().trim()
        )) {
            errors.rejectValue("confirmPassword", "field.match", "Passwords do not match");
            throw new PasswordException(errors);
        }
        if (!passwordEncoder.passwordEncoder().matches(request.getPassword(), user.getPassword())) {
            errors.rejectValue("password", "field.invalid", "Incorrect password");
            throw new PasswordException(errors);
        }
        passwordValidator.validate(request.getNewPassword(),errors);
        if (errors.hasErrors()) {
            throw new PasswordException(errors);
        }
        user.setPassword(passwordEncoder.passwordEncoder().encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User validateUser(Object target) {
        // 1. Null check with proper error handling
        Errors errors;
        if (target == null) {
            errors = new BeanPropertyBindingResult(new Object(), "user");
            errors.reject("field.required", "User object is required");
            throw new UserValidationException(errors);
        }

        // 2. Type-safe validation with proper error binding
        User user ;
        if (target instanceof RegistrationRequest registrationRequest) {
            errors = new BeanPropertyBindingResult(registrationRequest, "registrationRequest");
            user = userMapper.toEntity(registrationRequest);
            userRegistrationValidator.validate(user, errors);
        }
        else if (target instanceof UpdateUserRequest updateUserRequest) {
            errors = new BeanPropertyBindingResult(updateUserRequest, "updateUserRequest");
            user = userMapper.toEntity(updateUserRequest);
            userUpdateValidator.validate(user, errors);
        }
        else {
            // 3. Handle unexpected types gracefully
            throw new IllegalArgumentException(
                    "Unsupported target type for validation: " + target.getClass().getName()
            );
        }

        // 4. Check for validation errors
        if (errors.hasErrors()) {
            throw new UserValidationException(errors);
        }
        return user;
    }

    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user= validateUser(updateUserRequest);
        user= userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void deleteUser(Long userId) {
       Optional.of(userRepository.existsById(userId))
                .filter(Boolean::booleanValue)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
