package org.example.posts.service;

import lombok.AllArgsConstructor;
import org.example.posts.entity.Post;
import org.example.posts.entity.PostRequest;
import org.example.posts.entity.PostResponse;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> getAllPosts(String filter) {
        List<Post> posts;
        if (filter.equals("top")) {
            posts = postRepository.findAllByOrderByLikesCountDesc();
        } else {
            posts = postRepository.findAllByOrderByDateDesc();
        }
        return posts.stream().map(this::convertToPostResponse).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUser(Long userId, String filter) {
        List<Post> posts;
        if (filter.equals("top")) {
            posts = postRepository.findByUserIdOrderByLikesCountDesc(userId);
        } else {
            posts = postRepository.findByUserIdOrderByDateDesc(userId);
        }
        return posts.stream().map(this::convertToPostResponse).collect(Collectors.toList());
    }

    public Post createPost(Long userId, String content, String imageUrl) {
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

    public void likePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    private PostResponse convertToPostResponse(Post post) {
        User author = post.getUser();
        UserResponse authorResponse = new UserResponse(
                author.getId(), author.getName(), author.getLastName(), author.getEmail(), author.getAvatarColor());
        return new PostResponse(
                post.getId(), authorResponse, post.getContent(), post.getLikesCount(), post.getCommentsCount(),
                post.getImageUrl(), post.getDate(), false);
    }
}

