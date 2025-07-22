package com.example.facebook_demo.controller;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.service.GroupPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        GroupPostDTO created = groupPostService.createGroupPost(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupPostDTO>> getAllGroupPosts() {
        List<GroupPostDTO> posts = groupPostService.getAllGroupPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupPostDTO> getGroupPostById(@PathVariable int id) {
        GroupPostDTO post = groupPostService.getGroupPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupPostDTO> updateGroupPost(@PathVariable int id, @RequestBody GroupPostDTO dto) {
        GroupPostDTO updated = groupPostService.updateGroupPost(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroupPost(@PathVariable int id) {
        groupPostService.deleteGroupPost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

