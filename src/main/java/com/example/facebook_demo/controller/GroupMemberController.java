package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.facebook_demo.DTO.GroupMemberDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.GroupMemberService;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @PostMapping("/group/{groupId}")
    public ResponseEntity<APIResponse<GroupMemberDTO>> addMemberToGroup(@PathVariable int groupId,@RequestBody GroupMemberDTO dto) {
        GroupMemberDTO created = groupMemberService.addMemberToGroup(groupId, dto);
        APIResponse<GroupMemberDTO> apiResponse=new APIResponse<>("Group member created",created);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<GroupMemberDTO>>> getAll() {
        List<GroupMemberDTO> list = groupMemberService.getAll();
        APIResponse<List<GroupMemberDTO>> apiResponse=new APIResponse<>("Retrived all group members",list);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GroupMemberDTO>> getById(@PathVariable int id) {
        GroupMemberDTO dto = groupMemberService.getById(id);
        APIResponse<GroupMemberDTO> apiResponse=new APIResponse<>("Retrived group member",dto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<GroupMemberDTO>> update(@PathVariable int id, @RequestBody GroupMemberDTO dto) {
        GroupMemberDTO updated = groupMemberService.update(id, dto);
        APIResponse<GroupMemberDTO> apiResponse=new APIResponse<>("Group member updated",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable int id) {
        groupMemberService.delete(id);
        APIResponse<String> apiResponse=new APIResponse<>("Group member deleted successfully","Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT); 
    }
}
