package org.example.users.entity;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String avatarColor;

    public UserResponse(Long id, String name, String lastName, String email, String avatarColor) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.avatarColor = avatarColor;
    }
}
