package org.example.comments.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.posts.entity.Post;
import org.example.users.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String content;
    private LocalDateTime date = LocalDateTime.now();
    private Integer likesCount = 0;
}
