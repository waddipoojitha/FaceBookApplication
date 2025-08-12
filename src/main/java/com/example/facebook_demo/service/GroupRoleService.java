package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupRoleDTO;
import com.example.facebook_demo.DTO.GroupRoleRequestDTO;
import com.example.facebook_demo.entity.GroupRole;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.GroupRoleRepository;

@Service
public class GroupRoleService {
    @Autowired
    private GroupRoleRepository groupRoleRepo;

    public GroupRoleDTO createGroupRole(GroupRoleRequestDTO dto) {
        if(groupRoleRepo.existsByRole(dto.getRole())){
            throw new RuntimeException("Group role already exists");
        }
        GroupRole role=new GroupRole();
        role.setRole(dto.getRole());
        role.setDescription(dto.getDescription());
        role.setCreatedAt(LocalDateTime.now());

        GroupRole saved=groupRoleRepo.save(role);
        return mapToDTO(saved);
    }

    private GroupRoleDTO mapToDTO(GroupRole role) {
       return new GroupRoleDTO(
        role.getId(),
        role.getRole(),
        role.getDescription()
       );
    }

    public List<GroupRoleDTO> getAll() {
        return groupRoleRepo.findAll().stream()
        .map(role->new GroupRoleDTO(role.getId(),role.getRole(),role.getDescription()))
        .collect(Collectors.toList());
    }

    public GroupRoleDTO getById(int id) {
        GroupRole role=groupRoleRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group role not found")); 
        return mapToDTO(role);
    }

    public GroupRoleDTO updateGroupRole(int id, GroupRoleRequestDTO dto) {
            GroupRole role=groupRoleRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group role not found"));
            role.setRole(dto.getRole());
            role.setDescription(dto.getDescription());
            role.setUpdatedAt(LocalDateTime.now());
            GroupRole updated=groupRoleRepo.save(role);
            return mapToDTO(updated);
    }

    public void deleteById(int id) {
        GroupRole role=groupRoleRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Group role not found"));
        groupRoleRepo.delete(role);
    }
}