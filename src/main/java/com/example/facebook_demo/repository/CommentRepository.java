package com.example.facebook_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment,Integer>{

    List<Comment> findByParentIdAndParentType(int parentId, String parentType);

}
