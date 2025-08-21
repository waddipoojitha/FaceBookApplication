package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.User;

public interface GroupRepository extends JpaRepository<Group,Integer>{
    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group.id = :groupId")
    List<User> findUsersByGroupId(@Param("groupId") int groupId);

    Group findByDisplayNameAndDeletedAtIsNull(String displayName);

    List<Group> findByDeletedAtIsNull();

    Optional<Group> findByIdAndDeletedAtIsNull(int groupId);
}