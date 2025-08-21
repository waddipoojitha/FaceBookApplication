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
import com.example.facebook_demo.DTO.GroupRequestDTO;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<APIResponse<GroupDTO>> createGroup(@RequestBody GroupRequestDTO dto){
        GroupDTO created = groupService.createGroup(dto);
        APIResponse<GroupDTO> apiResponse=new APIResponse<>("Group created successfully",created);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    @GetMapping 
    public ResponseEntity<APIResponse<List<GroupDTO>>> getAll(){
        List<GroupDTO> list = groupService.getAll();
        APIResponse<List<GroupDTO>> apiResponse=new APIResponse<>("All groups retrived successfully",list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GroupDTO>> getById(@PathVariable int id){
        GroupDTO dto = groupService.getById(id);
        APIResponse<GroupDTO> apiResponse=new APIResponse<>("Group found",dto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/{id}/posts")
    public ResponseEntity<APIResponse<List<Post>>> getAllPostsInGroup(@PathVariable int id){
        List<Post> posts = groupService.getAllPostsInGroup(id);
        APIResponse<List<Post>> apiResponse=new APIResponse<>("Retrives posts in group",posts);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/{id}/users") 
    public ResponseEntity<APIResponse<List<String>>> getAllUsersInGroup(@PathVariable int id){
        List<String> users = groupService.getAllUsersInGroup(id);
        APIResponse<List<String>> apiResponse=new APIResponse<>("Retrived members in group",users);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteGroup(@PathVariable int id){
        groupService.deleteGroup(id);
        APIResponse<String> apiResponse=new APIResponse<>("Group deleted successfully","Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<GroupDTO>> updateGroup(@PathVariable int id,@RequestBody GroupRequestDTO dto){
        GroupDTO updated = groupService.updateGroup(id, dto);
        APIResponse<GroupDTO> apiResponse=new APIResponse<>("Group updates successfully",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } 
    
} 
