package org.example.posts.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.users.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String content;
    private Integer likesCount = 0;
    private Integer commentsCount = 0;
    private String imageUrl;
    private LocalDateTime date = LocalDateTime.now();
}
