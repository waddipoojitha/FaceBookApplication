package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/group-role")
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;

    @PostMapping
    public ResponseEntity<GroupRoleDTO> createGroupRole(@RequestBody GroupRoleDTO dto){
        return ResponseEntity.ok(groupRoleService.createGroupRole(dto));
    }
 
    @GetMapping
    public ResponseEntity<List<GroupRoleDTO>> getAll(){
        return ResponseEntity.ok(groupRoleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRoleDTO> getById(@PathVariable int id){
        return ResponseEntity.ok(groupRoleService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupRoleDTO> updateGroupRole(@PathVariable int id,@RequestBody GroupRoleDTO dto){
        return ResponseEntity.ok(groupRoleService.updateGroupRole(id,dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id){
        groupRoleService.deleteById(id);
        return ResponseEntity.ok("Group role deleted");
    }
}