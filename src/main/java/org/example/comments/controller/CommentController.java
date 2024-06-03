package org.example.comments.controller;

import lombok.AllArgsConstructor;
import org.example.comments.entity.Comment;
import org.example.comments.entity.CommentRequest;
import org.example.comments.entity.CommentResponse;
import org.example.comments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.addComment(commentRequest.getUserId(), postId, commentRequest.getContent());
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeComment(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long commentId = payload.get("commentId");
        commentService.likeComment(userId, commentId);
        return ResponseEntity.ok().build();
    }
}

