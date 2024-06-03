package org.example.comments.entity;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private String content;
}
