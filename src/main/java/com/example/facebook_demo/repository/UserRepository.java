package com.example.facebook_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{


    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User findByUsername(String username);
    
}
