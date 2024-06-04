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
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Long userId, @RequestParam String filter) {
        List<PostResponse> posts = postService.getAllPosts(userId, filter);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable Long userId, @RequestParam String filter) {
        List<PostResponse> posts = postService.getPostsByUser(userId, filter);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest.getUserId(), postRequest.getContent(), postRequest.getImageUrl());
        UserResponse author = new UserResponse(post.getUser().getId(), post.getUser().getName(), post.getUser().getLastName(), post.getUser().getEmail(), post.getUser().getAvatarColor());
        PostResponse response = new PostResponse(post.getId(), author, post.getContent(), post.getLikesCount(), post.getCommentsCount(), post.getImageUrl(), post.getDate(), false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestBody Long userId) {
        postService.likeOrDislikePost(userId, postId);
        return ResponseEntity.ok().build();
    }
}

