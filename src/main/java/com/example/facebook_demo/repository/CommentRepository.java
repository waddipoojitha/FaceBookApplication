package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer>{

    List<Comment> findByParentIdAndParentTypeAndDeletedAtIsNull(int parentId, String parentType);

    boolean existsByIdAndDeletedAtIsNull(int parentId);

    List<Comment> findByDeletedAtIsNull();

    Optional<Comment> findByIdAndDeletedAtIsNull(int id);

}
