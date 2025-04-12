package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.Config.PasswordEncoderConfig;
import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.enums.UserRoles;
import com.roadmap.backendapi.exception.user.*;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.request.user.changePasswordRequest;
import com.roadmap.backendapi.security.jwt.JwtService;
import com.roadmap.backendapi.security.jwt.tokenstore.TokenStore;
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
    private final PasswordEncoderConfig passwordEncoder;
    private final PasswordValidator passwordValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtService;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserRegistrationValidator userRegistrationValidator,
                           UserUpdateValidator userUpdateValidator, PasswordEncoderConfig passwordEncoder,
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
        User  user =userRepository.findById(userId)
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
            throw new LoginFailedException("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            throw new LoginFailedException("User not found");
        }catch (InternalAuthenticationServiceException e) {
            throw new AlreadyLoggedInException("User is already logged in");
        }
        catch (Exception e) {
            throw new LoginFailedException("Login failed: " + e);
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
        //TODO: check if the token is valid
        // make the user un authenticated
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
        return Optional.ofNullable(userRepository.findByEmail(email))
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
     * Registers a new user with the provided registration request.
     *
     * @param registrationRequest the RegistrationRequest object containing user details
     * @return the UserDTO object representing the registered user
     * @throws UserValidationException if user validation fails
     */
    @CachePut(value = "userCache", key = "#result.username")
    public UserDTO registerUser(RegistrationRequest registrationRequest)
    {
        User user= validateUser(registrationRequest);
        user.setRole(UserRoles.USER);
        user.setPassword(passwordEncoder.passwordEncoder().encode(registrationRequest.getPassword()));
        user.setEnabled(false);
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

    /**
     * Validates the user object based on the target type (RegistrationRequest or UpdateUserRequest).
     *
     * @param target the target object to validate
     * @return the validated User object
     * @throws UserValidationException if validation fails
     */
    private User validateUser(Object target) {

        Errors errors;
        if (target == null) {
            errors = new BeanPropertyBindingResult(new Object(), "user");
            errors.reject("field.required", "User object is required");
            throw new UserValidationException(errors);
        }

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
            throw new IllegalArgumentException(
                    "Unsupported target type for validation: " + target.getClass().getName()
            );
        }

        if (errors.hasErrors()) {
            throw new UserValidationException(errors);
        }
        return user;
    }

    /**
     * Updates the user information based on the provided UpdateUserRequest.
     *
     * @param id the ID of the user to update
     * @param updateUserRequest the UpdateUserRequest object containing updated user details
     * @return the UserDTO object representing the updated user
     * @throws UserNotFoundException if the user is not found
     */
    @CacheEvict(value = "userCache", key = "#result.username")
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user= validateUser(updateUserRequest);
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
