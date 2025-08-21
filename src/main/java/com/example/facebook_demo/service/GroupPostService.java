package com.example.facebook_demo.service;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.DTO.GroupPostRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
import com.example.facebook_demo.repository.GroupPostRepository;
import com.example.facebook_demo.repository.GroupRepository;
import com.example.facebook_demo.repository.PostRepository;
import com.example.facebook_demo.repository.UserRepository;
import com.example.facebook_demo.entity.GroupPost;

import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    public GroupPostDTO createGroupPost(GroupPostRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        Group group = groupRepo.findByIdAndDeletedAtIsNull(dto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        Post post = postRepo.findByIdAndDeletedAtIsNull(dto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupPost groupPost = modelMapper.map(dto, GroupPost.class);
        groupPost.setGroup(group);
        groupPost.setPost(post);
        groupPost.setUser(user);
        groupPost.setCreatedAt(LocalDateTime.now());
            
        return modelMapper.map(groupPostRepo.save(groupPost),GroupPostDTO.class);
    }

    public List<GroupPostDTO> getAllGroupPosts() {
        return groupPostRepo.findByDeletedAtIsNull().stream().map(gp -> modelMapper.map(gp, GroupPostDTO.class)).collect(Collectors.toList());
    }

    public GroupPostDTO getGroupPostById(int id) {
        GroupPost groupPost = groupPostRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("GroupPost not found"));
        return modelMapper.map(groupPost,GroupPostDTO.class);
    }

    public GroupPostDTO updateGroupPost(int id, GroupPostRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        GroupPost groupPost = groupPostRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("GroupPost not found"));
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!username.equals(groupPost.getUser().getUsername())){throw new UnauthorizedActionException("You can't update this group post");}
        groupPost.setUpdatedAt(LocalDateTime.now());

        Group group = groupRepo.findByIdAndDeletedAtIsNull(dto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        groupPost.setGroup(group);

        Post post = postRepo.findByIdAndDeletedAtIsNull(dto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        groupPost.setPost(post);

        return modelMapper.map(groupPostRepo.save(groupPost),GroupPostDTO.class);
    }

    public void deleteGroupPost(int id) {
        String username=SecurityUtils.getCurrentUsername();
        GroupPost groupPost = groupPostRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("GroupPost not found"));
        if(!username.equals(groupPost.getUser().getUsername())){throw new UnauthorizedActionException("You can't delete this group post");}
        groupPost.setDeletedAt(LocalDateTime.now());
        groupPostRepo.save(groupPost);
    }
}
