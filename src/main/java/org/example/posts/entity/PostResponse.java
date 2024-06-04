package org.example.posts.entity;

import lombok.Data;
import org.example.users.entity.UserResponse;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private UserResponse author;
    private String content;
    private Integer likesCount;
    private Integer commentsCount;
    private String imageUrl;
    private LocalDateTime date;
    private Boolean isLikedByMe;

    public PostResponse(Long id, UserResponse author, String content, Integer likesCount, Integer commentsCount, String imageUrl, LocalDateTime date, Boolean isLikedByMe) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.imageUrl = imageUrl;
        this.date = date;
        this.isLikedByMe = isLikedByMe;
    }
}
