package com.example.facebook_demo.service;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.DTO.GroupPostRequestDTO;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
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
                gp.getPost().getId()
        );
    }

    private GroupPost mapToEntity(GroupPostRequestDTO dto,User user) {
        GroupPost gp = new GroupPost();
        gp.setCreatedAt(LocalDateTime.now());
        gp.setUpdatedAt(LocalDateTime.now());

        Group group = groupRepo.findById(dto.getGroupId()).orElseThrow(() -> new RuntimeException("Group not found"));
        gp.setGroup(group);

        gp.setUser(user);

        Post post = postRepo.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
        gp.setPost(post);

        return gp;
    }

    public GroupPostDTO createGroupPost(GroupPostRequestDTO dto,String username) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user.getDeletedAt()!=null){throw new ResourceNotFoundException("User doesn't exist");}
        GroupPost gp = mapToEntity(dto,user);
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

    public GroupPostDTO updateGroupPost(int id, GroupPostRequestDTO dto,String username) {
        GroupPost gp = groupPostRepo.findById(id).orElseThrow(() -> new RuntimeException("GroupPost not found"));
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user.getDeletedAt()!=null){throw new ResourceNotFoundException("User doesn't exist");}

        gp.setUpdatedAt(LocalDateTime.now());

        Group group = groupRepo.findById(dto.getGroupId()).orElseThrow(() -> new RuntimeException("Group not found"));
        gp.setGroup(group);

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
