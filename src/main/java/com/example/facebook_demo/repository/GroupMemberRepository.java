package com.example.facebook_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.User;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer>{

    List<User> findByGroupId(int id);

    @Query("SELECT gm FROM GroupMember gm WHERE gm.group.id = :groupId")
    List<GroupMember> findGroupMembersByGroupId(@Param("groupId") int groupId);

    
}
