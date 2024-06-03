package org.example.posts.entity;

import lombok.Data;

@Data
public class PostRequest {
    private Long userId;
    private String content;
    private String imageUrl;
}
