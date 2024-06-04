package org.example.posts.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.users.entity.User;

@Entity
@Table(name = "post_likes")
@Data
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
