package org.example.comments.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.users.entity.User;

@Entity
@Table(name = "comment_likes")
@Data
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
