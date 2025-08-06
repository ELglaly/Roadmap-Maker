package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.exception.AppException;
import com.roadmap.backendapi.exception.user.InvalidTokenException;
import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.UserCreateDTO;
import com.roadmap.backendapi.request.user.UserUpdateDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.user.UseService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final static String BARSER = "Bearer ";
    
    private final UseService userService;

    public UserController(UseService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(new APIResponse("User found", userDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody @Validated UserCreateDTO registrationRequestDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(registrationRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse("User registered successfully", registeredUser));
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(new APIResponse("User registration failed", e.getErrors()));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("User registration failed", e.getMessage()));
        }
    }

    @GetMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody @Validated LoginRequest loginRequestDTO) {
        // Implement login and return token as response header
        try {
            String  token = userService.loginUser(loginRequestDTO);
            return ResponseEntity.ok().header("Authorization", BARSER+token)
                    .body(new APIResponse("User logged in successfully", null));
        } catch (AppException e) {
                return ResponseEntity.status(e.getStatus())
                    .body(new APIResponse("User login failed: "+e.getMessage() ,e ));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<APIResponse> logout( @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 1. Validate Authorization header
        if (authHeader == null || !authHeader.startsWith(BARSER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse("Missing or invalid Authorization header", null));
        }

        try {
            // 2. Extract token

            String token = StringUtils.delete(authHeader, BARSER).trim();

            // 3. Perform logout
            userService.logoutUser(token);

            // 4. Return success response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(new APIResponse("User logged out successfully", null));

        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest()
                    .body(new APIResponse("Invalid token", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(new APIResponse("Logout processing failed", e.getMessage()));
        }
    }
    

@DeleteMapping("/{id}")
public ResponseEntity<APIResponse> remove(@PathVariable Long id) {
    try {
        userService.deleteUser(id);
        return ResponseEntity.ok(new APIResponse("User deleted successfully", null));
    } catch (AppException e) {
        return ResponseEntity.status(e.getStatus()).body(new APIResponse("User deletion failed: " + e.getMessage(), null));
    }
    catch (DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("Database error: " + e.getMessage(), null));
    } catch (AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new APIResponse("Access denied: " + e.getMessage(), null));
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDto) {
        try {
            UserDTO updatedUser = userService.updateUser(id,userUpdateDto);
            return ResponseEntity.ok(new APIResponse("User updated successfully", updatedUser));
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(new APIResponse("User update failed: " , e.getErrors()));
        }
    }
}
