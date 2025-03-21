package com.roadmap.backendapi.controller;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.user.UseService;
import org.springframework.ai.chat.client.ChatClient;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UseService userService;
    private final  ChatClient  chatClient;

    public UserController(UseService userService, ChatClient chatClient, ChatClient chatClient1) {
        this.userService = userService;
        this.chatClient = chatClient1;
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

        try {
            String response = chatClient.prompt("Tell me a joke or a story").call().content();
            return ResponseEntity.ok(response);
        } catch (NonTransientAiException e) {
            // Handle insufficient balance or other non-transient errors
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body("Error: Insufficient balance to process the request. Please check your account.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Unable to process the request. Please try again later."+e.getMessage());
        }
    }
}