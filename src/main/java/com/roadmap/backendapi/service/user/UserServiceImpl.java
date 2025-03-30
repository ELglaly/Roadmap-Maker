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
    private final TokenStore tokenStore;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, UserRegistrationValidator userRegistrationValidator,
                           UserUpdateValidator userUpdateValidator, PasswordEncoderConfig passwordEncoder,
                           PasswordValidator passwordValidator, AuthenticationManager authenticationManager, com.roadmap.backendapi.security.jwt.JwtService jwtService, TokenStore tokenStore) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
        this.userUpdateValidator = userUpdateValidator;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.authenticationManager = authenticationManager;
        JwtService = jwtService;
        this.tokenStore = tokenStore;
    }

    public UserDTO getUserById(Long userId) {
        User  user =userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);
        return userMapper.toDTO(user);
    }

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

    @Override
    public void logoutUser(String token) {
        if(!tokenStore.isTokenPresent(token)){
            throw new AlreadyLoggedOutException("User is already logged out");
        }
        tokenStore.removeToken(token);
        // make the user un authenticated
        SecurityContextHolder.clearContext();

    }

    public UserDTO getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    @Cacheable(value = "user", key = "#username")
    public UserDTO getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }
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
    @CacheEvict(value = "userCache", key = "#result.username")
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user= validateUser(updateUserRequest);
        user= userRepository.save(user);
        return userMapper.toDTO(user);
    }
    @CacheEvict(value = "userCache", key = "#userId")
    public void deleteUser(Long userId) {
       Optional.of(userRepository.existsById(userId))
                .filter(Boolean::booleanValue)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
