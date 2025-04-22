package com.roadmap.backendapi.service.user.unit;
import com.roadmap.backendapi.exception.user.AlreadyLoggedInException;
import com.roadmap.backendapi.exception.user.LoginFailedException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.security.jwt.JwtService;
import com.roadmap.backendapi.service.user.UserService;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    LoginRequest loginRequest;
    @BeforeEach
    public void setUp()
    {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("loggedinuser");
        loginRequest.setPassword("password");
    }

    /**
     * Tests the loginUser method when the user is already logged in.
     * Expects an AlreadyLoggedInException to be thrown.
     */
    @Test
    public void test_loginUser_alreadyLoggedIn() {

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

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        LoginFailedException exception= assertThrows(LoginFailedException.class, ()
                -> userService.loginUser(loginRequest));

        assertEquals("Login failed: java.lang.RuntimeException: Unexpected error", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
        verifyNoMoreInteractions(authenticationManager);
    }

    /**
     * Tests the loginUser method when the user is not found.
     * Expects a LoginFailedException to be thrown.
     */
    @Test
    public void test_loginUser_userNotFound() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        LoginFailedException exception = assertThrows(LoginFailedException.class, ()
                -> userService.loginUser(loginRequest));

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
        verifyNoMoreInteractions(authenticationManager);
    }

    /**
     * Test case for loginUser method when authentication is successful.
     * This test verifies that the method returns a JWT token when the user
     * credentials are valid and the authentication is successful.
     */
    @Test
    public void test_loginUser_whenAuthenticationSuccessful_returnsJwtToken() {
        // Arrange

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
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(loginRequest.getUsername());
        verifyNoMoreInteractions(authenticationManager, jwtService);
    }


    /**
     * Test case for logoutUser method with null token.
     * This test verifies that the method clears the security context
     * even when provided with a null token.
     */
    //TODO
    @Test
    public void test_logoutUser_withNullToken() {
        // Arrange
        String nullToken = null;

        // Act
        userService.logoutUser(nullToken);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


}
