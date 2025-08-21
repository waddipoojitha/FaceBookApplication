package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByUsername(String username);
    
    Page<User> findByDeletedAtIsNull(Pageable pageable);

    Optional<User> findByIdAndDeletedAtIsNull(int userId);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);

    List<User> findByDeletedAtIsNull();

}