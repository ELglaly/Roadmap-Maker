package com.roadmap.backendapi.service.user.unit;

import com.roadmap.backendapi.Consts;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.UserRepository;

import java.util.Optional;

import com.roadmap.backendapi.response.ErrorResponse;
import com.roadmap.backendapi.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.roadmap.backendapi.exception.user.PasswordException;
import com.roadmap.backendapi.request.user.changePasswordRequest;
import com.roadmap.backendapi.validator.user.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Test class for UserServiceImpl.
 * This class contains unit tests for the changePassword method.
 */

@SpringBootTest
@ActiveProfiles("test")
public class PasswordTest {


    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    private User user;
    changePasswordRequest request;
    @BeforeEach
    public void setUp() {
        user = new User(); // You need to initialize the user object
        user.setId(1L);
        user.setPassword("Password@#010");
        request = new changePasswordRequest();
        request.setPassword("Password@#010");
        request.setNewPassword("NewPassword@#010");
        request.setConfirmPassword("NewPassword@#010");
    }


    /**
     * Test case for changePassword method when passwords match, current password is correct,
     * but new password fails validation the length validation.
     * This test verifies that a PasswordException is thrown when the new password
     * does not meet the validation criteria.
     */
    @ParameterizedTest
    @CsvSource({
            "'', ''",
            "'short', 'short'",
            "'ssword2', 'ssword2'",
            "'abc', 'abc'"
    })
    public void test_changePassword_WhenNewPasswordFailsLengthValidation(String newPassword, String confirmPassword) {

        request.setPassword(user.getPassword());
        request.setNewPassword(newPassword);
        request.setConfirmPassword(confirmPassword);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(request.getPassword(), user.getPassword())).thenReturn(true);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(request.getPassword(), user.getPassword())).thenReturn(true);
        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("password", "invalid.password", Consts.PasswordErrorMessage.PASSWORD_TOO_SHORT);
            return null;
        }).when(passwordValidator).validate(any(), any());

        PasswordException exception= assertThrows(PasswordException.class, ()
                -> userService.changePassword(user.getId(), request));

        Optional<ErrorResponse> error =exception.getErrors().stream().findFirst();
        assertTrue(error.isPresent(), "Expected at least one error");
        assertEquals(Consts.PasswordErrorMessage.PASSWORD_TOO_SHORT, error.get().getMessage());
        verify(userRepository).findById(user.getId());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(passwordValidator).validate(any(), any());
        verifyNoMoreInteractions(userRepository, passwordEncoder,passwordValidator);
    }


    @ParameterizedTest
    @CsvSource({
            "'PasswordFails', 'PasswordFails'",
            "'PasswordA#@Fails', 'PasswordA#@Fails'",
            "'asswordA#@ails', 'asswordA#@ails'",
            "'PasswordA010Fails', 'PasswordA010Fails'"
    })
    public void test_changePassword_WhenNewPasswordFailsValidation(String newPassword, String confirmPassword) {

        request.setPassword(user.getPassword());
        request.setNewPassword(newPassword);
        request.setConfirmPassword(confirmPassword);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(request.getPassword(), user.getPassword())).thenReturn(true);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(request.getPassword(), user.getPassword())).thenReturn(true);
        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("password", "invalid.password", Consts.PasswordErrorMessage.PASSWORD_CONSTRAINS);
            return null;
        }).when(passwordValidator).validate(any(), any());

        PasswordException exception= assertThrows(PasswordException.class, ()
                -> userService.changePassword(user.getId(), request));
        Optional<ErrorResponse> error =exception.getErrors().stream().findFirst();
        assertTrue(error.isPresent(), "Expected at least one error");
        assertEquals(Consts.PasswordErrorMessage.PASSWORD_CONSTRAINS, error.get().getMessage());
        verify(userRepository).findById(user.getId());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(passwordValidator).validate(any(), any());
        verifyNoMoreInteractions(userRepository, passwordEncoder,passwordValidator);
    }

    /**
     * Test case for changePassword method when the current password is incorrect.
     * This test verifies that a PasswordException is thrown when the provided
     * current password does not match the user's actual password.
     */
    @Test
    public void test_changePassword_incorrectCurrentPassword() {

        request.setPassword("incorrectPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(PasswordException.class, () -> userService.changePassword(user.getId(), request));
    }

    /**
     * Test case for changePassword method when passwords match, but current password is incorrect and new password is invalid.
     * This test verifies that a PasswordException is thrown when the current password doesn't match
     * and the new password fails validation.
     */
    @Test
    public void test_changePassword_incorrectCurrentPasswordAndInvalidNewPassword() {

        request.setPassword("wrongPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);
        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("newPassword", "invalid.password", "Invalid password");
            return null;
        }).when(passwordValidator).validate(any(), any());

        assertThrows(PasswordException.class, () -> userService.changePassword(user.getId(), request));
    }

    /**
     * Test case for changePassword method when the new password fails validation.
     * This test verifies that a PasswordException is thrown when the new password
     * does not meet the validation criteria set by the PasswordValidator.
     */
    @Test
    public void test_changePassword_newPasswordFailsValidation() {
        user.setPassword("oldPassword");
        changePasswordRequest request = new changePasswordRequest();
        request.setPassword("oldPassword");
        request.setNewPassword("invalidPassword");
        request.setConfirmPassword("invalidPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(anyString(), anyString())).thenReturn(true);
        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("newPassword", "invalid.password", "Password does not meet criteria");
            return null;
        }).when(passwordValidator).validate(anyString(), any(Errors.class));

        assertThrows(PasswordException.class, () -> userService.changePassword(user.getId(), request));
    }

    /**
     * Test case for changePassword method when passwords do not match.
     * This test verifies that a PasswordException is thrown when the new password
     * and confirmation password do not match.
     */
    @Test
    public void test_changePassword_passwordsDoNotMatch() {

        user.setPassword("oldPassword");
        changePasswordRequest request = new changePasswordRequest();
        request.setPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("differentPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when((passwordEncoder).matches(anyString(), anyString())).thenReturn(true);

        assertThrows(PasswordException.class, () -> userService.changePassword(user.getId(), request));
    }

    /**
     * Test case for changePassword method when the new password doesn't match the confirmation,
     * the current password is incorrect, but the new password passes validation.
     * This test verifies that a PasswordException is thrown with the appropriate error messages.
     */
    @Test
    public void testChangePassword_NewPasswordMismatchAndIncorrectCurrentPassword() {
        // Arrange
        Long userId = 1L;
        request.setConfirmPassword("NotMatchingPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        PasswordException exception = assertThrows(PasswordException.class, () -> {
            userService.changePassword(userId, request);
        });

        Optional<ErrorResponse> firstError = exception.getErrors().stream().findFirst();
        assertTrue(firstError.isPresent(), "Expected at least one error");

        assertEquals(Consts.PasswordErrorMessage.PASSWORD_MISMATCH, firstError.get().getMessage());


        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Test case for changePassword method when passwords don't match, old password is incorrect, and new password is invalid.
     * This test verifies that a PasswordException is thrown when the new password doesn't match the confirmation,
     * the old password is incorrect, and the new password is invalid.
     */
    @Test
    public void testChangePassword_PasswordsDoNotMatch_OldPasswordIncorrect_NewPasswordInvalid() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        PasswordException exception= assertThrows(PasswordException.class, () -> {
            userService.changePassword(user.getId(), request);
        });

        Optional<ErrorResponse> error = exception.getErrors().stream().findFirst();
        assertTrue(error.isPresent(), "Expected at least one error");
        assertEquals(Consts.PasswordErrorMessage.PASSWORD_INCORRECT, error.get().getMessage());


        verify(userRepository).findById(user.getId());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

}
