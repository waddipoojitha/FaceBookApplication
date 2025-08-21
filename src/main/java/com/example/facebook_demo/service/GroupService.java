package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupDTO;
import com.example.facebook_demo.DTO.GroupMemberRequestDTO;
import com.example.facebook_demo.DTO.GroupRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.GroupPost;
import com.example.facebook_demo.entity.GroupRole;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceAlreadyExistsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
import com.example.facebook_demo.repository.GroupMemberRepository;
import com.example.facebook_demo.repository.GroupPostRepository;
import com.example.facebook_demo.repository.GroupRepository;
import com.example.facebook_demo.repository.GroupRoleRepository;
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

    @Autowired private GroupMemberService groupMemberService;
    @Autowired private GroupRoleRepository groupRoleRepo;
    @Autowired private ModelMapper modelMapper;

    public GroupDTO createGroup(GroupRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        User creator = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(groupRepo.findByDisplayNameAndDeletedAtIsNull(dto.getDisplayName())!=null){throw new ResourceAlreadyExistsException("Group with display name "+dto.getDisplayName()+" already exists.");}
        Group group=modelMapper.map(dto,Group.class);
        group.setCreatedBy(creator);
        group.setCreatedAt(LocalDateTime.now());

        Group savedGroup=groupRepo.save(group);

        GroupRole role=groupRoleRepo.findByRoleAndDeletedAtIsNull("Admin").orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        int roleId=role.getId();
        GroupMemberRequestDTO memberDTO=new GroupMemberRequestDTO(savedGroup.getId(),creator.getId(),roleId);
        groupMemberService.addMemberToGroup(memberDTO);

        return modelMapper.map(savedGroup,GroupDTO.class);
    }

    public List<GroupDTO> getAll() {
        return groupRepo.findByDeletedAtIsNull().stream().map(group->modelMapper.map(group,GroupDTO.class)).collect(Collectors.toList());
    }

    public GroupDTO getById(int id) { 
        Group group=groupRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        return modelMapper.map(group,GroupDTO.class);
    }

    public List<Post> getAllPostsInGroup(int id) {
        Group group=groupRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group not found")); 
        List<GroupPost> posts=groupPostRepo.findByGroupIdAndDeletedAtIsNull(id);
        return posts.stream().map(post->post.getPost()).collect(Collectors.toList());
    }

    public List<String> getAllUsersInGroup(int groupId) {
        Group group=groupRepo.findByIdAndDeletedAtIsNull(groupId).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        List<GroupMember> members = groupMemberRepo.findGroupMembersByGroupIdAndDeletedAtIsNull(groupId);
        return members.stream().map(gm -> gm.getUser().getUsername()).collect(Collectors.toList());
    }

    public void deleteGroup(int id) {
        String username=SecurityUtils.getCurrentUsername();
        Group group=groupRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        if(!username.equals(group.getCreatedBy().getUsername())){throw new UnauthorizedActionException("You can't delete this group");}
        group.setDeletedAt(LocalDateTime.now());
        groupRepo.save(group);
    }
 
    public GroupDTO updateGroup(int id, GroupRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        Group group=groupRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group not found"));
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user==group.getCreatedBy()){
            if(dto.getDisplayName()!=null){
                group.setDisplayName(dto.getDisplayName());
            }
            if(dto.getDescription()!=null){
                group.setDescription(dto.getDescription());
            }
            group.setUpdatedAt(LocalDateTime.now());
            return modelMapper.map(groupRepo.save(group),GroupDTO.class);
        }
        else{
            throw new UnauthorizedActionException("You can't update this group");
        }
    }
}
