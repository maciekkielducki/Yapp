package org.example.posts.entity;

import lombok.Data;
import org.example.users.entity.UserResponse;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private UserResponse author; // Zmieniono z User na UserResponse
    private String content;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean isLikedByMe; // Zmieniono z String na Boolean
    private String imageUrl;
    private LocalDateTime date; // Zmieniono z String na LocalDateTime

    public PostResponse(Long id, UserResponse author, String content, Integer likesCount, Integer commentsCount, String imageUrl, LocalDateTime date, Boolean isLikedByMe) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.isLikedByMe = isLikedByMe;
        this.imageUrl = imageUrl;
        this.date = date;
    }
}
