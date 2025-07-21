package com.example.facebook_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.service.GroupPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-posts")
public class GroupPostController {

    @Autowired
    private GroupPostService groupPostService;

    @PostMapping
    public ResponseEntity<GroupPostDTO> createGroupPost(@RequestBody GroupPostDTO dto) {
        return ResponseEntity.ok(groupPostService.createGroupPost(dto));
    }

    @GetMapping
    public ResponseEntity<List<GroupPostDTO>> getAllGroupPosts() {
        return ResponseEntity.ok(groupPostService.getAllGroupPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupPostDTO> getGroupPostById(@PathVariable int id) {
        return ResponseEntity.ok(groupPostService.getGroupPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupPostDTO> updateGroupPost(@PathVariable int id, @RequestBody GroupPostDTO dto) {
        return ResponseEntity.ok(groupPostService.updateGroupPost(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable int id) {
        groupPostService.deleteGroupPost(id);
        return ResponseEntity.ok("GroupPost deleted successfully");
    }
}

