package com.example.facebook_demo.service;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.repository.GroupPostRepository;
import com.example.facebook_demo.repository.GroupRepository;
import com.example.facebook_demo.repository.PostRepository;
import com.example.facebook_demo.repository.UserRepository;
import com.example.facebook_demo.entity.GroupPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupPostService{

    @Autowired
    private GroupPostRepository groupPostRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    private GroupPostDTO mapToDTO(GroupPost gp) {
        return new GroupPostDTO(
                gp.getId(),
                gp.getGroup().getId(),
                gp.getUser().getId(),
                gp.getPost().getId(),
                gp.getCreatedAt(),
                gp.getUpdatedAt()
        );
    }

    private GroupPost mapToEntity(GroupPostDTO dto) {
        GroupPost gp = new GroupPost();
        gp.setCreatedAt(LocalDateTime.now());
        gp.setUpdatedAt(LocalDateTime.now());

        Group group = groupRepo.findById(dto.getGroupId()).orElseThrow(() -> new RuntimeException("Group not found"));
        gp.setGroup(group);

        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        gp.setUser(user);

        Post post = postRepo.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
        gp.setPost(post);

        return gp;
    }

    public GroupPostDTO createGroupPost(GroupPostDTO dto) {
        GroupPost gp = mapToEntity(dto);
        return mapToDTO(groupPostRepo.save(gp));
    }

    public List<GroupPostDTO> getAllGroupPosts() {
        return groupPostRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public GroupPostDTO getGroupPostById(int id) {
        GroupPost gp = groupPostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("GroupPost not found"));
        return mapToDTO(gp);
    }

    public GroupPostDTO updateGroupPost(int id, GroupPostDTO dto) {
        GroupPost gp = groupPostRepo.findById(id).orElseThrow(() -> new RuntimeException("GroupPost not found"));

        gp.setUpdatedAt(LocalDateTime.now());

        Group group = groupRepo.findById(dto.getGroupId()).orElseThrow(() -> new RuntimeException("Group not found"));
        gp.setGroup(group);

        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        gp.setUser(user);

        Post post = postRepo.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
        gp.setPost(post);

        return mapToDTO(groupPostRepo.save(gp));
    }

    public void deleteGroupPost(int id) {
        GroupPost gp = groupPostRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("GroupPost not found"));
        groupPostRepo.delete(gp);
    }
}
