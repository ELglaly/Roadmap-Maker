package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.utils.Const;
import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.user.User;
import com.roadmap.backendapi.exception.user.*;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.UserCreateDTO;
import com.roadmap.backendapi.request.user.ChangePasswordRequest;
import com.roadmap.backendapi.request.user.UserUpdateDTO;
import com.roadmap.backendapi.security.jwt.JwtService;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Objects;
import java.util.Optional;

/**
 * UserServiceImpl is a service class that implements the UseService interface.
 * It provides methods for managing user accounts, including registration, login, logout, and user information retrieval.
 */


@Service
public class UserServiceImpl implements UseService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserUpdateValidator userUpdateValidator;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtService;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserRegistrationValidator userRegistrationValidator,
                           UserUpdateValidator userUpdateValidator, PasswordEncoder passwordEncoder,
                           PasswordValidator passwordValidator, AuthenticationManager authenticationManager, com.roadmap.backendapi.security.jwt.JwtService jwtService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
        this.userUpdateValidator = userUpdateValidator;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.authenticationManager = authenticationManager;
        JwtService = jwtService;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDTO object representing the user
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public UserDTO getUserById(Long userId) {
        User user =userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);
        return userMapper.toDTO(user);
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequest the LoginRequest object containing the username and password
     * @return a JWT token if login is successful
     * @throws LoginFailedException if login fails due to invalid credentials
     */
    @Override
    public String loginUser(LoginRequest loginRequest) {
        try {
            // 1. Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
            if(authentication.isAuthenticated()){
                return JwtService.generateToken(loginRequest.getUsername());
            }
            else {
                throw new LoginFailedException("Invalid username or password");
            }
        } catch (BadCredentialsException e) {
            throw new LoginFailedException("Invalid username or password : " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            throw new LoginFailedException("User not found : "+ e.getMessage());
        } catch (InternalAuthenticationServiceException e) {
            throw new AlreadyLoggedInException("User is already logged in : " + e.getMessage());
        } catch (Exception e) {
            throw new LoginFailedException("Login failed :" + e.getMessage());
        }
    }

    /**
     * Logs out a user by removing their token from the token store.
     *
     * @param token the JWT token to be removed
     * @throws AlreadyLoggedOutException if the user is already logged out
     */
    @Override
    public void logoutUser(String token) {
        JwtService.blacklistToken(token);
        // . Clear authentication context
        SecurityContextHolder.clearContext();
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return the UserDTO object representing the user
     * @throws UserNotFoundException if the user is not found
     */
    public UserDTO getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByUserContactEmail(email))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the UserDTO object representing the user
     * @throws UserNotFoundException if the user is not found
     */
    @Cacheable(value = "user", key = "#username")
    public UserDTO getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }


    /**
     * Registers a new user.
     *
     * @param userCreateDto the UserCreateDTO object containing user details
     * @return the UserDTO object representing the registered user
     * @throws UserValidationException if validation fails
     */
    @CachePut(value = "userCache", key = "#result.username")
    public UserDTO registerUser(UserCreateDTO userCreateDto)
    {
        User user= validateUser(userCreateDto);
        user.getUserSecurity().setPasswordHash(passwordEncoder.encode(userCreateDto.getPasswordHash()));
        user= userRepository.save(user);
        return userMapper.toDTO(user);
    }


    /**
     * Changes the password of a user.
     *
     * @param userId the ID of the user whose password is to be changed
     * @param request the changePasswordRequest object containing the new password and confirmation
     * @throws PasswordException if password validation fails
     */
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Errors errors = new BeanPropertyBindingResult(request, "newPassword");
        if (!Objects.equals(
                request.getNewPassword().trim(),
                request.getConfirmPassword().trim()
        )) {
            errors.rejectValue("confirmPassword", "password.match", Const.PasswordErrorMessages.PASSWORD_MISMATCH);
            throw new PasswordException(errors);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getUserSecurity().getPasswordHash())) {
            errors.rejectValue("password", "password.invalid", Const.PasswordErrorMessages.PASSWORD_INCORRECT);
            throw new PasswordException(errors);
        }
        passwordValidator.validate(request.getNewPassword(),errors);
        if (errors.hasErrors()) {
            throw new PasswordException(errors);
        }
        user.getUserSecurity().setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Validates the user object based on the target type (RegistrationRequest or UpdateUserRequest).
     *
     * @param target the target object to validate
     * @return the validated User object
     * @throws UserValidationException if validation fails
     */
    public User validateUser(Object target) {
        if (target == null) {
            Errors errors = new BeanPropertyBindingResult(new Object(), "user");
            errors.reject("field.required", "User object is required");
            throw new UserValidationException(errors);
        }

        return switch (target) {
            case UserCreateDTO registrationRequest -> {
                Errors errors = new BeanPropertyBindingResult(registrationRequest, "registrationRequest");
                User user = userMapper.toEntity(registrationRequest);
                userRegistrationValidator.validate(user, errors);

                if (errors.hasErrors()) {
                    throw new UserValidationException(errors);
                }
                yield user;
            }

            case UserUpdateDTO userUpdateDto -> {
                Errors errors = new BeanPropertyBindingResult(userUpdateDto, "updateUserRequest");
                User user = userMapper.toEntity(userUpdateDto);
                userUpdateValidator.validate(user, errors);

                if (errors.hasErrors()) {
                    throw new UserValidationException(errors);
                }
                yield user;
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported target type for validation: " + target.getClass().getName()
            );
        };
    }


    /**
     * Updates a user's information.
     *
     * @param id the ID of the user to update
     * @param userUpdateDto the UserUpdateDTO object containing updated user details
     * @return the UserDTO object representing the updated user
     * @throws UserValidationException if validation fails
     */
    @CacheEvict(value = "userCache", key = "#result.username")
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDto) {
        User user= validateUser(userUpdateDto);
        user= userRepository.save(user);
        return userMapper.toDTO(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    @CacheEvict(value = "userCache", key = "#userId")
    public void deleteUser(Long userId) {
       Optional.of(userRepository.existsById(userId))
                .filter(Boolean::booleanValue)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
