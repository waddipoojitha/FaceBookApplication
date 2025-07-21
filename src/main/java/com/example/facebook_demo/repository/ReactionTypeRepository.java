package com.example.facebook_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.ReactionType;

public interface ReactionTypeRepository extends JpaRepository<ReactionType,Integer>{

    boolean findByType(String type);

    boolean existsByType(String type);
    
}
