package com.roadmap.backendapi.service.user.unit;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.entity.enums.UserRoles;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.service.user.UserService;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class RegistrationTest {


        @Mock
        private PasswordEncoder passwordEncoder;


        @Mock
        private UserRegistrationValidator userRegistrationValidator;

        @Mock
        private UserMapper userMapper;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserService userService;


        User user;
        UserDTO userDTO ;
        @BeforeEach
        public void setUp() {
            user = new User(); // You need to initialize the user object
            user.setId(1L);
            user.setPassword("Password@#010");
            user.setEmail("test@example.com");
            user.setUsername("test@585");
            userDTO = new UserDTO();

        }


        /**
         * Test case for registerUser method when user validation fails.
         * This test verifies that a UserValidationException is thrown when
         * the user registration validator reports errors.
         */
        @Test
        public void test_registerUser_validationFails() {
            // Arrange
            RegistrationRequest registrationRequest = new RegistrationRequest();
            when(userMapper.toEntity(registrationRequest)).thenReturn(user);

            doAnswer(invocation -> {
                Errors errors = invocation.getArgument(1);
                errors.rejectValue("username", "field.invalid", "Invalid username");
                return null;
            }).when(userRegistrationValidator).validate(eq(user), any(Errors.class));

            // Act & Assert
            assertThrows(UserValidationException.class, () -> {
                userService.registerUser(registrationRequest);
            });

            verify(userMapper).toEntity(registrationRequest);
            verify(userRegistrationValidator).validate(eq(user), any(Errors.class));
            verifyNoMoreInteractions(userRepository, passwordEncoder);
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
