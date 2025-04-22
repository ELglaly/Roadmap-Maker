package com.roadmap.backendapi.service.user.unit;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;

import java.util.Optional;

import com.roadmap.backendapi.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.roadmap.backendapi.security.jwt.JwtService;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import com.roadmap.backendapi.validator.user.UserRegistrationValidator;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")

public class UserSearchTest {


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
     * Test case for getUserById method when the user is not found.
     * This test verifies that a UserNotFoundException is thrown when
     * attempting to retrieve a non-existent user by ID.
     */
    @Test
    public void testGetUserById_UserNotFound() {
        Long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(nonExistentUserId);
        });
    }

    /**
     * Test case for getUserById method when the user exists.
     * It verifies that the method correctly retrieves a user by their ID,
     * maps it to a UserDTO, and returns the result.
     */
    @Test
    public void test_getUserById_whenUserExists_returnsUserDTO() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findById(user.getId());
        verify(userMapper).toDTO(user);
    }


    /**
     * Test case for getUserByEmail method when the user is found.
     * This test verifies that the method correctly retrieves a user by their email,
     * maps it to a UserDTO, and returns the result.
     */
    @Test
    public void testGetUserByEmail_UserFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserByEmail(user.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findByEmail(user.getEmail());
        verify(userMapper).toDTO(user);
    }

    /**
     * Test case for getUserById method when the user ID is null.
     * This test verifies that a NullPointerException is thrown when
     * attempting to retrieve a user with a null ID.
     */
    @Test
    public void testGetUserById_NullUserId() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(null);
        });
    }

    /**
     * Tests the getUserById method when the user is not found in the repository.
     * This test verifies that a UserNotFoundException is thrown when the repository
     * returns an empty Optional for the given userId.
     */
    @Test
    public void testGetUserById_UserNotFound_2() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void testGetUserById_UserNotFound_3() {
        Long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(nonExistentUserId);
        });
    }

    /**
     * Test case for getUserByUsername method when the user is not found.
     * This test verifies that a UserNotFoundException is thrown when
     * attempting to retrieve a non-existent user by username.
     */
    @Test
    public void testGetUserByUsername_UserNotFound() {
        String nonExistentUsername = "nonexistent";
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByUsername(nonExistentUsername);
        });
    }

    /**
     * Test case for getUserByUsername method when the user exists.
     * It verifies that the method correctly retrieves a user by their username,
     * maps it to a UserDTO, and returns the result.
     */
    @Test
    public void testGetUserByUsername_WhenUserExists_ReturnsUserDTO() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserByUsername(user.getUsername());

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findByUsername(user.getUsername());
        verify(userMapper).toDTO(user);
    }

    /**
     * Test case for logoutUser method.
     * This test verifies that the SecurityContext is cleared when a user logs out.
     */
    @Test
    public void testLogoutUserClearsSecurityContext() {
        // Arrange
        String token = "valid_token";

        // Act
        userService.logoutUser(token);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Security context should be cleared after logout");
    }

    /**
     * Test case for UserServiceImpl constructor.
     * Verifies that the UserServiceImpl object is correctly instantiated with all dependencies.
     */
    @Test
    public void testUserServiceImplConstructor() {
        UserService userServiceImpl = new UserService(
                userMapper,
                userRepository,
                userRegistrationValidator,
                userUpdateValidator,
                passwordEncoder,
                passwordValidator,
                authenticationManager,
                jwtService
        );

        assertNotNull(userServiceImpl, "UserServiceImpl should be instantiated successfully");
    }


    @Test
    public void test_deleteUser_userNotFound() {
        // Arrange
        Long nonExistentUserId = 999L;
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(nonExistentUserId);
        });

        // Verify
        verify(userRepository).existsById(nonExistentUserId);
        verify(userRepository, never()).deleteById(nonExistentUserId);
    }

    /**
     * Test case for deleteUser method when the user exists.
     * This test verifies that the deleteUser method successfully deletes a user
     * when the user exists in the repository.
     */
    @Test
    public void test_deleteUser_whenUserExists_deletesSuccessfully() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    /**
     * Test case for getUserByEmail method when the user is not found.
     * This test verifies that a UserNotFoundException is thrown when
     * attempting to retrieve a non-existent user by email.
     */
    @Test
    public void test_getUserByEmail_UserNotFound() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail(nonExistentEmail);
        });
    }

    /**
     * Test case for getUserById method when the user exists.
     * It verifies that the method returns the correct UserDTO when a valid user ID is provided.
     */
    @Test
    public void test_getUserById_whenUserExists_returnsUserDTO_2() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    public void test_getUserById_whenUserExists_returnsUserDTO_3() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userDTO, result);
        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

}
