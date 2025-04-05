package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.exception.user.InvalidTokenException;
import com.roadmap.backendapi.exception.user.UserValidationException;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    .body(new APIResponse("User login failed: "+e.getMessage() + e.toString() ,e ));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<APIResponse> logout(
            HttpServletRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 1. Validate Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse("Missing or invalid Authorization header", null));
        }

        try {
            // 2. Extract token
            String token = authHeader.substring(7);

            // 3. Perform logout
            userService.logoutUser(token);

            // 4. Return success response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(new APIResponse("User logged out successfully", null));

        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest()
                    .body(new APIResponse("Invalid token", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new APIResponse("Logout processing failed", null));
        }
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
