package org.example.comments.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentMessage {
    private Long postId;
    private CommentResponse commentResponse;
}
