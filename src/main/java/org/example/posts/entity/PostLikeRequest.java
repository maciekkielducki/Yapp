package org.example.posts.entity;

import lombok.Data;

@Data
public class PostLikeRequest
{
    private Long userId;
    private Long postId;
}
