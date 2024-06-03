package org.example.posts.controller;

import lombok.AllArgsConstructor;
import org.example.posts.entity.Post;
import org.example.posts.entity.PostRequest;
import org.example.posts.entity.PostResponse;
import org.example.posts.service.PostService;
import org.example.users.entity.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(defaultValue = "new") String filter) {
        List<PostResponse> posts = postService.getAllPosts(filter);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId, @RequestParam(defaultValue = "new") String filter) {
        List<PostResponse> posts = postService.getPostsByUser(userId, filter);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest.getUserId(), postRequest.getContent(), postRequest.getImageUrl());
        UserResponse author = new UserResponse(post.getUser().getId(), post.getUser().getName(), post.getUser().getLastName(), post.getUser().getEmail(), post.getUser().getAvatarColor());
        PostResponse response = new PostResponse(
                post.getId(),
                author,
                post.getContent(),
                post.getLikesCount(),
                post.getCommentsCount(),
                post.getImageUrl(),
                post.getDate(),
                false
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long postId = payload.get("postId");
        postService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }
}

