package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.GroupRole;

public interface GroupRoleRepository extends JpaRepository<GroupRole,Integer>{

    boolean existsByRole(String role);

    List<GroupRole> findByDeletedAtIsNull();

    Optional<GroupRole> findByIdAndDeletedAtIsNull(int roleId);

    Optional<GroupRole> findByRoleAndDeletedAtIsNull(String string);
    
}
