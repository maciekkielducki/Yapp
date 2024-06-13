package org.example.comments.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.comments.entity.Comment;
import org.example.comments.entity.CommentLike;
import org.example.comments.entity.CommentMessage;
import org.example.comments.entity.CommentResponse;
import org.example.comments.repository.CommentLikeRepository;
import org.example.comments.repository.CommentRepository;
import org.example.posts.entity.Post;
import org.example.posts.repository.PostRepository;
import org.example.users.entity.User;
import org.example.users.entity.UserResponse;
import org.example.users.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<CommentResponse> getCommentsByPost(Long postId, Long userId, Long loggedUserId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByDateDesc(postId);
        return comments.stream()
                .map(comment -> convertToCommentResponse(comment, userId, loggedUserId))
                .collect(Collectors.toList());
    }

    public Comment addComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);
        log.info("Saved comment {}", savedComment);

        CommentResponse commentResponse = convertToCommentResponse(savedComment, userId, null);
        CommentMessage commentMessage = new CommentMessage(postId, commentResponse);
        log.info("Sending message to /topic/comments: " + commentMessage);

        messagingTemplate.convertAndSend("/topic/comments", commentMessage);
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        return savedComment;
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
            log.info("Liked comment: {} by user: {}", newCommentLike.getComment().getId(), userId);
        }
    }

    private long getCommentLikesCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    private boolean isCommentLikedByUser(Long userId, Long commentId) {
        return commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isPresent();
    }

    private CommentResponse convertToCommentResponse(Comment comment, Long userId, @Nullable Long loggedUserId) {
        User user = comment.getUser();
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getName(), user.getLastName(), user.getEmail(), user.getAvatarColor());
        long likesCount = getCommentLikesCount(comment.getId());
        boolean isLikedByMe = false;
        if (loggedUserId != null && loggedUserId != -1) {
            isLikedByMe = isCommentLikedByUser(loggedUserId, comment.getId());
        }
        return new CommentResponse(
                comment.getId(), comment.getContent(), userResponse, (int) likesCount,
                isLikedByMe, comment.getDate());
    }
}
