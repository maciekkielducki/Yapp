package org.example.comments.entity;

import lombok.Data;
import org.example.users.entity.UserResponse;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private UserResponse user;
    private Integer likesCount;
    private boolean isLikedByMe;
    private LocalDateTime date;

    public CommentResponse(Long id, String content, UserResponse user, Integer likesCount, boolean isLikedByMe, LocalDateTime date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.likesCount = likesCount;
        this.isLikedByMe = isLikedByMe;
        this.date = date;
    }

}
