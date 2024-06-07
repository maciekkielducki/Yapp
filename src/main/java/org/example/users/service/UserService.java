package org.example.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.users.entity.User;
import org.example.users.entity.UserRequest;
import org.example.users.entity.UserResponse;
import org.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserResponse(user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAvatarColor());
    }

    public UserResponse loginUser(String email, String hashedPassword) {
        User user = userRepository.findByEmail(email);
        log.info("Found user: {}", user);
        if (user != null && user.getPassword().equals(hashedPassword)) {
            return new UserResponse(user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAvatarColor());
        }
        return null;
    }

    public String registerUser(UserRequest newUserRequest) {
        if (userRepository.findByEmail(newUserRequest.getEmail()) != null) {
            log.info("User already exists: {}", newUserRequest.getEmail());
            return "EMAIL_TAKEN";
        }
        User newUser = new User();
        newUser.setName(newUserRequest.getName());
        newUser.setLastName(newUserRequest.getLastName());
        newUser.setEmail(newUserRequest.getEmail());
        newUser.setPassword(newUserRequest.getPassword());
        newUser.setAvatarColor(newUserRequest.getAvatarColor());
        userRepository.save(newUser);
        log.info("Created new user: {}", newUser);
        return "OK";
    }
}

