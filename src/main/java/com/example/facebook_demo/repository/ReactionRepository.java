package com.example.facebook_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction,Integer>{

    List<Reaction> findByParentIdAndParentType(int parentId, String parentType);

}
