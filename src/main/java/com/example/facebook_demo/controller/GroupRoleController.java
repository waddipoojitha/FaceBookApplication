package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.GroupRoleDTO;
import com.example.facebook_demo.service.GroupRoleService;

@RestController
@RequestMapping("/api/group-roles")
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;

    @PostMapping
    public ResponseEntity<GroupRoleDTO> createGroupRole(@RequestBody GroupRoleDTO dto){
        GroupRoleDTO created = groupRoleService.createGroupRole(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
 
    @GetMapping
    public ResponseEntity<List<GroupRoleDTO>> getAll(){
        List<GroupRoleDTO> roles = groupRoleService.getAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRoleDTO> getById(@PathVariable int id){
        GroupRoleDTO role = groupRoleService.getById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupRoleDTO> updateGroupRole(@PathVariable int id,@RequestBody GroupRoleDTO dto){
        GroupRoleDTO updated = groupRoleService.updateGroupRole(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id){
        groupRoleService.deleteById(id);
        return new ResponseEntity<>("Group role deleted",HttpStatus.NO_CONTENT);
    }
}