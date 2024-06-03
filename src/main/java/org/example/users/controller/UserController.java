package org.example.users.controller;

import lombok.AllArgsConstructor;
import org.example.users.entity.UserRequest;
import org.example.users.entity.UserResponse;
import org.example.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String hashedPassword = payload.get("password");
        UserResponse user = userService.loginUser(email, hashedPassword);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest newUser) {
        String result = userService.registerUser(newUser);
        if ("EMAIL_TAKEN".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("EMAIL_TAKEN");
        }
        return ResponseEntity.ok("OK");
    }
}

