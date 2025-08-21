package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.GroupRoleDTO;
import com.example.facebook_demo.DTO.GroupRoleRequestDTO;
import com.example.facebook_demo.entity.GroupRole;
import com.example.facebook_demo.exception.ResourceAlreadyExistsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.GroupRoleRepository;

@Service
public class GroupRoleService {
    @Autowired
    private GroupRoleRepository groupRoleRepo;

    @Autowired
    private ModelMapper modelMapper;

    public GroupRoleDTO createGroupRole(GroupRoleRequestDTO dto) {
        if(groupRoleRepo.existsByRole(dto.getRole())){
            throw new ResourceAlreadyExistsException("Group role already exists");
        }
        GroupRole role=modelMapper.map(dto,GroupRole.class);
        role.setCreatedAt(LocalDateTime.now());

        GroupRole saved=groupRoleRepo.save(role);
        return modelMapper.map(saved,GroupRoleDTO.class);
    }

    public List<GroupRoleDTO> getAll() {
        return groupRoleRepo.findByDeletedAtIsNull().stream()
        .map(role->modelMapper.map(role,GroupRoleDTO.class)).collect(Collectors.toList());
    }

    public GroupRoleDTO getById(int id) {
        GroupRole role=groupRoleRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group role not found")); 
        return modelMapper.map(role,GroupRoleDTO.class);
    }

    public GroupRoleDTO updateGroupRole(int id, GroupRoleRequestDTO dto) {
            GroupRole role=groupRoleRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group role not found"));
            if(dto.getRole()!=null){
                role.setRole(dto.getRole());
            }
            if(dto.getDescription()!=null){
                role.setDescription(dto.getDescription());
            }
            role.setUpdatedAt(LocalDateTime.now());
            GroupRole updated=groupRoleRepo.save(role);
            return modelMapper.map(updated,GroupRoleDTO.class);
    }

    public void deleteById(int id) {
        GroupRole role=groupRoleRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Group role not found"));
        role.setDeletedAt(LocalDateTime.now());
        groupRoleRepo.save(role);
    }
}