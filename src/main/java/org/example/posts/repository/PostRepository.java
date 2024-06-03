package org.example.posts.repository;

import org.example.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdOrderByDateDesc(Long userId);
    List<Post> findAllByOrderByDateDesc();
    List<Post> findAllByOrderByLikesCountDesc();
    List<Post> findByUserIdOrderByLikesCountDesc(Long userId);

}
