package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupDTO;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.GroupPost;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.GroupMemberRepository;
import com.example.facebook_demo.repository.GroupPostRepository;
import com.example.facebook_demo.repository.GroupRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GroupPostRepository groupPostRepo;
    @Autowired
    private GroupMemberRepository groupMemberRepo;

    public GroupDTO createGroup(GroupDTO dto) {
        User creator=userRepo.findById(dto.getCreatedBy()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Group group=new Group(creator, dto.getDisplayName(), dto.getDescription());
        group.setCreatedAt(LocalDateTime.now());

        return mapToDTO(groupRepo.save(group));
    }

    private GroupDTO mapToDTO(Group group) {
        return new GroupDTO(group.getId(),group.getCreatedBy().getId(),group.getDisplayName(),group.getDescription(),group.getCreatedAt(),group.getUpdatedAt());
    }

    public List<GroupDTO> getAll() {
        return groupRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GroupDTO getById(int id) { 
        Group group=groupRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("group not found"));
        return mapToDTO(group);
    }

    public List<Post> getAllPostsInGroup(int id) {
        List<GroupPost> posts=groupPostRepo.findByGroupId(id);
        return posts.stream().map(post->post.getPost()).collect(Collectors.toList());
    }

    public List<String> getAllUsersInGroup(int groupId) {
        List<GroupMember> members = groupMemberRepo.findGroupMembersByGroupId(groupId);
        return members.stream().map(gm -> gm.getUser().getUsername()).collect(Collectors.toList());
    }

    public void deleteGroup(int id) {
        Group group=groupRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        groupRepo.delete(group);
    }
 
    public GroupDTO updateGroup(int id, GroupDTO dto) {
        Group group=groupRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        group.setDisplayName(dto.getDisplayName());
        group.setDescription(dto.getDescription());
        group.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(group);
    }
}
