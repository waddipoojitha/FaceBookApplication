package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.facebook_demo.DTO.GroupMemberDTO;
import com.example.facebook_demo.service.GroupMemberService;

@RestController
@RequestMapping("/api/group-member")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @PostMapping("/group/{groupId}")
    public ResponseEntity<GroupMemberDTO> addMemberToGroup(@PathVariable int groupId,
                                                           @RequestBody GroupMemberDTO dto) {
        return ResponseEntity.ok(groupMemberService.addMemberToGroup(groupId, dto));
    }

    @GetMapping
    public ResponseEntity<List<GroupMemberDTO>> getAll() {
        return ResponseEntity.ok(groupMemberService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupMemberDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(groupMemberService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMemberDTO> update(@PathVariable int id, @RequestBody GroupMemberDTO dto) {
        return ResponseEntity.ok(groupMemberService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        groupMemberService.delete(id);
        return ResponseEntity.ok("Group member deleted successfully.");
    }
}
