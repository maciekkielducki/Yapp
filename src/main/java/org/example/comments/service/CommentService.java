package org.example.comments.service;

import lombok.AllArgsConstructor;
import org.example.comments.entity.Comment;
import org.example.comments.entity.CommentLike;
import org.example.comments.entity.CommentResponse;
import org.example.comments.repository.CommentLikeRepository;
import org.example.comments.repository.CommentRepository;
import org.example.posts.entity.Post;
import org.example.posts.repository.PostRepository;
import org.example.users.entity.User;
import org.example.users.entity.UserResponse;
import org.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    public List<CommentResponse> getCommentsByPost(Long postId, Long userId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByDateDesc(postId);
        return comments.stream()
                .map(comment -> convertToCommentResponse(comment, userId))
                .collect(Collectors.toList());
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

    @Transactional
    public void likeComment(Long userId, Long commentId) {
        CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId).orElse(null);
        if (commentLike != null) {
            commentLikeRepository.delete(commentLike);
        } else {
            CommentLike newCommentLike = new CommentLike();
            newCommentLike.setUser(userRepository.findById(userId).orElseThrow());
            newCommentLike.setComment(commentRepository.findById(commentId).orElseThrow());
            commentLikeRepository.save(newCommentLike);
        }
    }

    private long getCommentLikesCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    private boolean isCommentLikedByUser(Long userId, Long commentId) {
        return commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isPresent();
    }

    private CommentResponse convertToCommentResponse(Comment comment, Long userId) {
        User user = comment.getUser();
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAvatarColor());
        long likesCount = getCommentLikesCount(comment.getId());
        boolean isLikedByMe = isCommentLikedByUser(userId, comment.getId());
        return new CommentResponse(
                comment.getId(), comment.getContent(), userResponse, (int) likesCount,
                isLikedByMe, comment.getDate());
    }
}
