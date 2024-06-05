package org.example.comments.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.users.entity.UserResponse;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private Integer likesCount;
    private Boolean isLikedByMe;
    private LocalDateTime date;
}
