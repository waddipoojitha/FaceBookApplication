package com.example.facebook_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.User;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer>{

    List<User> findByGroupId(int id);

    List<GroupMember> findGroupMembersByGroupIdAndDeletedAtIsNull(int groupId);

    List<GroupMember> findByDeletedAtIsNull();

    Optional<GroupMember> findByIdAndDeletedAtIsNull(int id);

    
}
