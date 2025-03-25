package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.response.ErrorResponse;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UseService {

    private final UserMapper userMapper;
    private final   UserRepository userRepository;
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserUpdateValidator userUpdateValidator;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserRegistrationValidator userRegistrationValidator, UserUpdateValidator userUpdateValidator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
        this.userUpdateValidator = userUpdateValidator;
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
        validateUser(registrationRequest);
        User user= userMapper.toEntity(registrationRequest);
        user= userRepository.save(user);
        return userMapper.toDTO(user);

    }

    private void validateUser(Object target) {
        // 1. Null check with proper error handling
        Errors errors;
        if (target == null) {
            errors = new BeanPropertyBindingResult(new Object(), "user");
            errors.reject("field.required", "User object is required");
            throw new UserValidationException(errors);
        }

        // 2. Type-safe validation with proper error binding

        if (target instanceof RegistrationRequest registrationRequest) {
            errors = new BeanPropertyBindingResult(registrationRequest, "registrationRequest");
            userRegistrationValidator.validate(registrationRequest, errors);
        }
        else if (target instanceof UpdateUserRequest updateUserRequest) {
            errors = new BeanPropertyBindingResult(updateUserRequest, "updateUserRequest");
            userUpdateValidator.validate(updateUserRequest, errors);
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
    }

    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        validateUser(updateUserRequest);
        User user= userMapper.toEntity(updateUserRequest);
     //   user= userRepository.save(user);
        return userMapper.toDTO(user);

    }

    public void deleteUser(Long userId) {
       Optional.of(userRepository.existsById(userId))
                .filter(Boolean::booleanValue)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
