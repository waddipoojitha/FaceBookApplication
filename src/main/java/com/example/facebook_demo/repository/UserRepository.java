package com.example.facebook_demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.facebook_demo.entity.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,Integer>{

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email LIKE '%_deleted' AND u.deletedAt <= :cutoff")
    List<User> findUsersMarkedForPermanentDeletion(@Param("cutoff") LocalDateTime cutoff);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE email LIKE '%_deleted' AND deleted_at <= :cutoff", nativeQuery = true)
    void deleteUsersMarkedForPermanentDeletion(@Param("cutoff") LocalDateTime cutoff);
}