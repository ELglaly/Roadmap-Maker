package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.request.user.LoginRequest;
import com.roadmap.backendapi.request.user.UserCreateDTO;
import com.roadmap.backendapi.request.user.UserUpdateDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.user.UseService;

import static com.roadmap.backendapi.utils.Const.BEARER;

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

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody @Validated UserCreateDTO registrationRequestDTO) {
            UserDTO registeredUser = userService.registerUser(registrationRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse("User registered successfully", registeredUser));
    }


    @GetMapping("/login")
    public ResponseEntity.BodyBuilder login(@RequestBody @Validated LoginRequest loginRequestDTO) {
            String  token = userService.loginUser(loginRequestDTO);
            return ResponseEntity.ok().header("Authorization", BEARER+token);
    }


    @GetMapping("/logout")
    public ResponseEntity<APIResponse> logout( @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // 1. Validate Authorization header
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse("Missing or invalid Authorization header", null));
        }

            String token = StringUtils.delete(authHeader, BEARER).trim();

            // 3. Perform logout
            userService.logoutUser(token);

            // 4. Return success response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .body(new APIResponse("User logged out successfully", null));
    }

@DeleteMapping("/{id}")
public ResponseEntity<APIResponse> remove(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new APIResponse("User deleted successfully", null));
}

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDto) {
            UserDTO updatedUser = userService.updateUser(id,userUpdateDto);
            return ResponseEntity.ok(new APIResponse("User updated successfully", updatedUser));
    }
}
