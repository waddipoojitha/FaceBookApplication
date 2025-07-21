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

import com.example.facebook_demo.DTO.GroupDTO;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO dto){
        return ResponseEntity.ok(groupService.createGroup(dto));
    }
    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAll(){
        return ResponseEntity.ok(groupService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getById(@PathVariable int id){
        return ResponseEntity.ok(groupService.getById(id));
    }
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getAllPostsInGroup(@PathVariable int id){
        return ResponseEntity.ok(groupService.getAllPostsInGroup(id));
    }
    @GetMapping("/{id}/users") 
    public ResponseEntity<List<String>> getAllUsersInGroup(@PathVariable int id){
        return ResponseEntity.ok(groupService.getAllUsersInGroup(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable int id){
        groupService.deleteGroup(id);
        return ResponseEntity.ok("Group deleted");
    }
    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable int id,@RequestBody GroupDTO dto){
        return ResponseEntity.ok(groupService.updateGroup(id,dto));
    } 
    
} 
