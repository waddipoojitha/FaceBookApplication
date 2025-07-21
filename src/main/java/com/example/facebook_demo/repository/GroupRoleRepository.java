package com.example.facebook_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.GroupRole;

public interface GroupRoleRepository extends JpaRepository<GroupRole,Integer>{

    boolean existsByRole(String role);
    
}
