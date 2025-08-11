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

import com.example.facebook_demo.DTO.GroupRoleDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.GroupRoleService;

@RestController
@RequestMapping("/api/group-roles")
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;

    @PostMapping
    public ResponseEntity<APIResponse<GroupRoleDTO>> createGroupRole(@RequestBody GroupRoleDTO dto){
        GroupRoleDTO created = groupRoleService.createGroupRole(dto);
        APIResponse<GroupRoleDTO> apiResponse=new APIResponse<>("Group role created successfully",created);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
 
    @GetMapping
    public ResponseEntity<APIResponse<List<GroupRoleDTO>>> getAll(){
        List<GroupRoleDTO> roles = groupRoleService.getAll();
        APIResponse<List<GroupRoleDTO>> apiResponse=new APIResponse<>("Retrived all group roles",roles);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<GroupRoleDTO>> getById(@PathVariable int id){
        GroupRoleDTO role = groupRoleService.getById(id);
        APIResponse<GroupRoleDTO> apiResponse=new APIResponse<>("Retrived group role",role);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<GroupRoleDTO>> updateGroupRole(@PathVariable int id,@RequestBody GroupRoleDTO dto){
        GroupRoleDTO updated = groupRoleService.updateGroupRole(id, dto);
        APIResponse<GroupRoleDTO> apiResponse=new APIResponse<>("Updated group role",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<APIResponse<String>> deleteById(@PathVariable int id){
        groupRoleService.deleteById(id);
        APIResponse<String> apiResponse=new APIResponse<>("Group role deleted","Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT);
    }
}