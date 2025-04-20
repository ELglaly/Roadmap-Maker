package com.roadmap.backendapi.service.user.unit;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.service.user.UserService;
import com.roadmap.backendapi.validator.user.UserUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ActiveProfiles("test")
public class UpdateUserTest {


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
         * Test case for updateUser method when the input is null.
         * This test verifies that a UserValidationException is thrown when
         * attempting to update a user with a null UpdateUserRequest.
         */
        @Test
        public void test_updateUser_nullInput() {
            Long userId = 1L;
            UpdateUserRequest nullRequest = null;

            assertThrows(UserValidationException.class, () -> {
                userService.updateUser(userId, nullRequest);
            });
        }

        /**
         * Test case for updateUser method when the user is successfully updated.
         * This test verifies that the method correctly validates the user,
         * saves the updated user, and returns the mapped UserDTO.
         */
        @Test
        public void test_updateUser_successfulUpdate() {
            // Arrange
            Long userId = 1L;
            UpdateUserRequest updateUserRequest = new UpdateUserRequest(); // fill with test data if needed
            User mappedUser = new User(); // this is the result of userMapper.toEntity(updateUserRequest)
            User savedUser = new User(); // this is the user after save
            UserDTO expectedDto = new UserDTO();

            // Stubbing the internals
            when(userMapper.toEntity(updateUserRequest)).thenReturn(mappedUser);
            doNothing().when(userUpdateValidator).validate(eq(mappedUser), any());
            when(userRepository.save(mappedUser)).thenReturn(savedUser);
            when(userMapper.toDTO(savedUser)).thenReturn(expectedDto);

            // Act
            UserDTO result = userService.updateUser(userId, updateUserRequest);

            // Assert
            assertNotNull(result);
            assertEquals(expectedDto, result);

            verify(userMapper).toEntity(updateUserRequest);
            verify(userUpdateValidator).validate(eq(mappedUser), any());
            verify(userRepository).save(mappedUser);
            verify(userMapper).toDTO(savedUser);
        }
    }

