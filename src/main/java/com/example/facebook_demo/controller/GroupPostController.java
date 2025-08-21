package com.example.facebook_demo.controller;

import com.example.facebook_demo.DTO.GroupPostDTO;
import com.example.facebook_demo.DTO.GroupPostRequestDTO;
import com.example.facebook_demo.response.APIResponse;
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
    public ResponseEntity<APIResponse<GroupPostDTO>> createGroupPost(@RequestBody GroupPostRequestDTO dto) {
        GroupPostDTO created = groupPostService.createGroupPost(dto);
        APIResponse<GroupPostDTO> apiResponse=new APIResponse<>("Group post created",created);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<GroupPostDTO>>> getAllGroupPosts() {
        List<GroupPostDTO> posts = groupPostService.getAllGroupPosts();
        APIResponse<List<GroupPostDTO>> apiResponse=new APIResponse<>("Retrived all group posts",posts);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GroupPostDTO>> getGroupPostById(@PathVariable int id) {
        GroupPostDTO post = groupPostService.getGroupPostById(id);
        APIResponse<GroupPostDTO> apiResponse=new APIResponse<>("Retrived group post",post);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<GroupPostDTO>> updateGroupPost(@PathVariable int id, @RequestBody GroupPostRequestDTO dto) {
        GroupPostDTO updated = groupPostService.updateGroupPost(id, dto);
        APIResponse<GroupPostDTO> apiResponse=new APIResponse<>("Updated group post",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteGroupPost(@PathVariable int id) {
        groupPostService.deleteGroupPost(id);
        APIResponse<String> apiResponse=new APIResponse<>("Deleted group post successfully","Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT);
    }
}

