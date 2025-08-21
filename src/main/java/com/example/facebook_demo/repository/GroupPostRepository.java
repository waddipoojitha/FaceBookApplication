package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.GroupPost;

public interface GroupPostRepository extends JpaRepository<GroupPost,Integer>{

    List<GroupPost> findByGroupIdAndDeletedAtIsNull(int id);

    List<GroupPost> findByDeletedAtIsNull();

    Optional<GroupPost> findByIdAndDeletedAtIsNull(int id);

}
