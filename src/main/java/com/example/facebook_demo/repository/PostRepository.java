package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Post;

public interface PostRepository extends JpaRepository<Post,Integer>{

    List<Post> findAllByUserId(int userId);

    List<Post> findByDeletedAtIsNull();

    List<Post> findAllByUserIdAndDeletedAtIsNull(int userId);

    boolean existsByIdAndDeletedAtIsNull(int parentId);

    Optional<Post> findByIdAndDeletedAtIsNull(int postId);

}
