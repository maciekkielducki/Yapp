package org.example.posts.controller;

import lombok.AllArgsConstructor;
import org.example.posts.entity.Post;
import org.example.posts.entity.PostLikeRequest;
import org.example.posts.entity.PostRequest;
import org.example.posts.entity.PostResponse;
import org.example.posts.service.PostService;
import org.example.users.entity.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Long loggedUserId, @RequestParam String filter) {
        List<PostResponse> posts = postService.getAllPosts(loggedUserId, filter);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{userId}")
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

    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@RequestBody PostLikeRequest postLikeRequest) {
        postService.likeOrDislikePost(postLikeRequest.getUserId(), postLikeRequest.getPostId());
        return ResponseEntity.ok().build();
    }
}

