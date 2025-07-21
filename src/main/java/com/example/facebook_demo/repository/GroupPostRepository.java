package com.example.facebook_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.GroupPost;

public interface GroupPostRepository extends JpaRepository<GroupPost,Integer>{

    List<GroupPost> findByGroupId(int id);

}
