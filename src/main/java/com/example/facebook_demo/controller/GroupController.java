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
        GroupDTO created = groupService.createGroup(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAll(){
        List<GroupDTO> list = groupService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getById(@PathVariable int id){
        GroupDTO dto = groupService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> getAllPostsInGroup(@PathVariable int id){
        List<Post> posts = groupService.getAllPostsInGroup(id);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/{id}/users") 
    public ResponseEntity<List<String>> getAllUsersInGroup(@PathVariable int id){
        List<String> users = groupService.getAllUsersInGroup(id);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable int id){
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable int id,@RequestBody GroupDTO dto){
        GroupDTO updated = groupService.updateGroup(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    } 
    
} 
