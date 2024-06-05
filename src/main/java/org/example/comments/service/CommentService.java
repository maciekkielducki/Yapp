package org.example.comments.service;

import lombok.AllArgsConstructor;
import org.example.comments.entity.Comment;
import org.example.comments.entity.CommentResponse;
import org.example.comments.repository.CommentRepository;
import org.example.posts.entity.Post;
import org.example.posts.repository.PostRepository;
import org.example.users.entity.User;
import org.example.users.entity.UserResponse;
import org.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByDateDesc(postId);
        return comments.stream().map(this::convertToCommentResponse).collect(Collectors.toList());
    }

    public Comment addComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public void likeComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepository.save(comment);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        User user = comment.getUser();
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAvatarColor());
        return new CommentResponse(
                comment.getId(), comment.getContent(), userResponse, comment.getLikesCount(),
                false, comment.getDate());
    }
}


