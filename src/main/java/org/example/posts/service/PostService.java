package org.example.posts.service;

import lombok.AllArgsConstructor;
import org.example.posts.entity.Post;
import org.example.posts.entity.PostLike;
import org.example.posts.entity.PostRequest;
import org.example.posts.entity.PostResponse;
import org.example.posts.repository.PostLikeRepository;
import org.example.posts.repository.PostRepository;
import org.example.users.entity.User;
import org.example.users.entity.UserResponse;
import org.example.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService
{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public List<PostResponse> getAllPosts(String filter)
    {
        List<Post> posts;
        if (filter.equals("top")) {
            posts = postRepository.findAllByOrderByLikesCountDesc();
        } else {
            posts = postRepository.findAllByOrderByDateDesc();
        }
        return posts.stream().map(post -> convertToPostResponse(post)).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUser(Long userId, String filter)
    {
        List<Post> posts;
        if (filter.equals("top")) {
            posts = postRepository.findByUserIdOrderByLikesCountDesc(userId);
        } else {
            posts = postRepository.findByUserIdOrderByDateDesc(userId);
        }
        return posts.stream().map(post -> convertToPostResponseByUserId(post, userId)).collect(Collectors.toList());
    }

    public Post createPost(Long userId, String content, String imageUrl)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLikesCount(0);
        post.setCommentsCount(0);
        post.setDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    public void likeOrDislikePost(Long userId, Long postId)
    {
        Post post = postRepository.findById(postId).orElseThrow();

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            dislikePost(postId, userId, post);
        } else {
            likePost(userId, post);
        }

        postRepository.save(post);
    }

    private void likePost(Long userId, Post post)
    {
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(userRepository.findById(userId).orElseThrow());
        postLikeRepository.save(postLike);
        post.setLikesCount(post.getLikesCount() + 1);
    }

    private void dislikePost(Long postId, Long userId, Post post)
    {
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        post.setLikesCount(post.getLikesCount() - 1);
    }

    private PostResponse convertToPostResponse(Post post)
    {
        UserResponse authorResponse = new UserResponse(
                post.getUser().getId(), post.getUser().getName(), post.getUser().getLastName(), post.getUser().getEmail(), post.getUser().getAvatarColor());
        boolean isLikedByMe = postLikeRepository.existsByPostId(post.getId());
        return new PostResponse(
                post.getId(), authorResponse, post.getContent(), post.getLikesCount(), post.getCommentsCount(),
                post.getImageUrl(), post.getDate(), isLikedByMe);
    }

    private PostResponse convertToPostResponseByUserId(Post post, Long userId)
    {
        UserResponse authorResponse = new UserResponse(
                post.getUser().getId(), post.getUser().getName(), post.getUser().getLastName(), post.getUser().getEmail(), post.getUser().getAvatarColor());
        boolean isLikedByMe = postLikeRepository.existsByPostIdAndUserId(post.getId(), userId);
        return new PostResponse(
                post.getId(), authorResponse, post.getContent(), post.getLikesCount(), post.getCommentsCount(),
                post.getImageUrl(), post.getDate(), isLikedByMe);
    }
}

