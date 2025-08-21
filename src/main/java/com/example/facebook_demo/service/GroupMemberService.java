package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupMemberDTO;
import com.example.facebook_demo.DTO.GroupMemberRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.GroupRole;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
import com.example.facebook_demo.repository.GroupMemberRepository;
import com.example.facebook_demo.repository.GroupRepository;
import com.example.facebook_demo.repository.GroupRoleRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class GroupMemberService {

    @Autowired
    private GroupMemberRepository groupMemberRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupRoleRepository groupRoleRepo;

    @Autowired
    private ModelMapper modelMapper;

    public GroupMemberDTO addMemberToGroup( GroupMemberRequestDTO dto) {
        Group group = groupRepo.findByIdAndDeletedAtIsNull(dto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + dto.getGroupId()));
        User user = userRepo.findByIdAndDeletedAtIsNull(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        GroupRole groupRole = groupRoleRepo.findByIdAndDeletedAtIsNull(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setGroupRole(groupRole);
        member.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(groupMemberRepo.save(member),GroupMemberDTO.class);
    }

    public List<GroupMemberDTO> getAll() {
        return groupMemberRepo.findByDeletedAtIsNull().stream().map(gm->modelMapper.map(gm, GroupMemberDTO.class)).collect(Collectors.toList());
    }

    public GroupMemberDTO getById(int id) {
        GroupMember member = groupMemberRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        return modelMapper.map(member,GroupMemberDTO.class);
    }

    public GroupMemberDTO update(int id, int roleId) {
        String username=SecurityUtils.getCurrentUsername();
        GroupMember member = groupMemberRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        GroupRole role = groupRoleRepo.findByIdAndDeletedAtIsNull(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        if(!member.getUser().getUsername().equals(username)){throw new UnauthorizedActionException("You can't update this group member");}
        member.setGroupRole(role);
        member.setUpdatedAt(LocalDateTime.now());

        return modelMapper.map(groupMemberRepo.save(member),GroupMemberDTO.class);
    }

    public void delete(int id) {
        String username=SecurityUtils.getCurrentUsername();
        GroupMember member = groupMemberRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        if(!member.getUser().getUsername().equals(username)){throw new UnauthorizedActionException("You can't remove this group member");}
        member.setDeletedAt(LocalDateTime.now());
        groupMemberRepo.save(member);
    }
}
