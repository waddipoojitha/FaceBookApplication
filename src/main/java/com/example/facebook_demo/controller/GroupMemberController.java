package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.facebook_demo.DTO.GroupMemberDTO;
import com.example.facebook_demo.service.GroupMemberService;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @PostMapping("/group/{groupId}")
    public ResponseEntity<GroupMemberDTO> addMemberToGroup(@PathVariable int groupId,@RequestBody GroupMemberDTO dto) {
        GroupMemberDTO created = groupMemberService.addMemberToGroup(groupId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupMemberDTO>> getAll() {
        List<GroupMemberDTO> list = groupMemberService.getAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupMemberDTO> getById(@PathVariable int id) {
        GroupMemberDTO dto = groupMemberService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMemberDTO> update(@PathVariable int id, @RequestBody GroupMemberDTO dto) {
        GroupMemberDTO updated = groupMemberService.update(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        groupMemberService.delete(id);
        return new ResponseEntity<>("Group member deleted successfully.",HttpStatus.NO_CONTENT); 
    }
}
