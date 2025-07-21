package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupMemberDTO;
import com.example.facebook_demo.entity.Group;
import com.example.facebook_demo.entity.GroupMember;
import com.example.facebook_demo.entity.GroupRole;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
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

    public GroupMemberDTO addMemberToGroup(int groupId, GroupMemberDTO dto) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        GroupRole groupRole = groupRoleRepo.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setGroupRole(groupRole);
        member.setCreatedAt(LocalDateTime.now());

        return mapToDTO(groupMemberRepo.save(member));
    }

    private GroupMemberDTO mapToDTO(GroupMember member) {
        return new GroupMemberDTO(
                member.getId(),
                member.getGroup().getId(),
                member.getUser().getId(),
                member.getGroupRole().getId(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }

    public List<GroupMemberDTO> getAll() {
        return groupMemberRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GroupMemberDTO getById(int id) {
        GroupMember member = groupMemberRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        return mapToDTO(member);
    }

    public GroupMemberDTO update(int id, GroupMemberDTO dto) {
        GroupMember member = groupMemberRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        GroupRole role = groupRoleRepo.findById(dto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));
        member.setGroupRole(role);
        member.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(groupMemberRepo.save(member));
    }

    public void delete(int id) {
        GroupMember member = groupMemberRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group member not found with id: " + id));
        groupMemberRepo.delete(member);
    }
}
