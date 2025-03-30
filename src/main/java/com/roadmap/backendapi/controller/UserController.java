package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.user.UseService;
import org.springframework.ai.chat.client.ChatClient;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UseService userService;

    public UserController(UseService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(new APIResponse("User found", userDTO));
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat() {
        String message = "Tell me a joke or a story";
        Prompt prompt = new Prompt(message);

        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody RegistrationRequest registrationRequestDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(registrationRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse("User registered successfully", registeredUser));
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(new APIResponse("User registration failed", e.getErrors()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("User registration failed", e.getMessage()));
        }
    }

    @GetMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequest loginRequestDTO) {
        // Implement login and return token as response header
        try {
            String  token = userService.loginUser(loginRequestDTO);
            return ResponseEntity.ok().header("Authorization", "Bearer "+token)
                    .body(new APIResponse("User logged in successfully", null));
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new APIResponse("User login failed: "+e.getMessage() ,null ));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<APIResponse> logout(HttpServletRequest request) {
        // Implement logout
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        userService.logoutUser(token);
        return ResponseEntity.ok(new APIResponse("User logged out successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> remove(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new APIResponse("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("User deletion failed: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable Long id, @RequestBody UpdateUserRequest updateRequestDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id,updateRequestDTO);
            return ResponseEntity.ok(new APIResponse("User updated successfully", updatedUser));
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(new APIResponse("User update failed: " , e.getErrors()));
        }
    }
}
