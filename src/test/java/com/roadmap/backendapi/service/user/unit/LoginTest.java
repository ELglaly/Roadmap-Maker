package com.roadmap.backendapi.service.user.unit;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.entity.enums.UserRoles;
import com.roadmap.backendapi.exception.user.AlreadyLoggedInException;
import com.roadmap.backendapi.exception.user.LoginFailedException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.security.jwt.JwtService;
import com.roadmap.backendapi.service.user.UserService;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl.
 * This class contains unit tests for the loginUser and logoutUser methods.
 */

@SpringBootTest
@ActiveProfiles("test")
public class LoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private UserRegistrationValidator userRegistrationValidator;

    @Mock
    private UserUpdateValidator userUpdateValidator;
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    /**
     * Tests the loginUser method when the user is already logged in.
     * Expects an AlreadyLoggedInException to be thrown.
     */
    @Test
    public void test_loginUser_alreadyLoggedIn() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("loggedinuser");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InternalAuthenticationServiceException("User is already logged in"));

        assertThrows(AlreadyLoggedInException.class, () -> userService.loginUser(loginRequest));
    }

    /**
     * Test case for loginUser method when authentication fails.
     * This test verifies that a LoginFailedException is thrown when
     * the authentication is not successful.
     */
    @Test
    public void test_loginUser_authenticationFails() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Act & Assert
        assertThrows(LoginFailedException.class, () -> {
            userService.loginUser(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    /**
     * Tests the loginUser method when invalid credentials are provided.
     * Expects a LoginFailedException to be thrown.
     */
    @Test
    public void test_loginUser_invalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        assertThrows(LoginFailedException.class, () -> userService.loginUser(loginRequest));
    }

    /**
     * Tests the loginUser method when an unexpected exception occurs.
     * Expects a LoginFailedException to be thrown.
     */
    @Test
    public void test_loginUser_unexpectedException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(LoginFailedException.class, () -> userService.loginUser(loginRequest));
    }

    /**
     * Tests the loginUser method when the user is not found.
     * Expects a LoginFailedException to be thrown.
     */
    @Test
    public void test_loginUser_userNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(LoginFailedException.class, () -> userService.loginUser(loginRequest));
    }

    /**
     * Test case for loginUser method when authentication is successful.
     * This test verifies that the method returns a JWT token when the user
     * credentials are valid and the authentication is successful.
     */
    @Test
    public void test_loginUser_whenAuthenticationSuccessful_returnsJwtToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        String expectedToken = "test.jwt.token";
        when(jwtService.generateToken(loginRequest.getUsername())).thenReturn(expectedToken);

        // Act
        String result = userService.loginUser(loginRequest);

        // Assert
        assertEquals(expectedToken, result);
    }




    /**
     * Test case for logoutUser method with null token.
     * This test verifies that the method clears the security context
     * even when provided with a null token.
     */
    @Test
    public void test_logoutUser_withNullToken() {
        // Arrange
        String nullToken = null;

        // Act
        userService.logoutUser(nullToken);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Test case for registerUser method when a valid registration request is provided.
     * This test verifies that the method correctly creates a new user, sets appropriate values,
     * saves the user to the repository, and returns the mapped UserDTO.
     */
    // TODO : check
    @Test
    public void test_registerUser_withValidRequest_returnsUserDTO() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setPassword("password123");

        User user = new User();
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");

        when(userService.validateUser(registrationRequest)).thenReturn(user);
        doNothing().when(userRegistrationValidator).validate(eq(registrationRequest), any());
        when(userMapper.toEntity(registrationRequest)).thenReturn(user);
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(passwordEncoder).thenReturn(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.registerUser(registrationRequest);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getRole() == UserRoles.USER &&
                        !savedUser.isEnabled() &&
                        savedUser.getPassword() != null && !savedUser.getPassword().equals("password123")
        ));
        verify(userMapper).toDTO(user);
    }
}
