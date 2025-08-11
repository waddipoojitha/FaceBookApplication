package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.facebook_demo.DTO.LoginDTO;
import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDTO>> signup(@RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.signup(userDTO);

        APIResponse<UserDTO> apiResponse = new APIResponse<>("User registered succcessfully",registeredUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDTO loginDTO) {
        try{
            String token=userService.verify(loginDTO);
            APIResponse<String> apiResponse = new APIResponse<>("User logged in successfully", token);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch(RuntimeException ex){
            APIResponse<String> apiResponse=new APIResponse<>(ex.getMessage(),null);
            return new ResponseEntity<>(apiResponse,HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Get all users",description = "Fetches all users from the DB")
    @GetMapping
    public ResponseEntity<APIResponse<List<UserDTO>>> getAll() {
        List<UserDTO> users = userService.getAllUsers();
        APIResponse<List<UserDTO>> apiResponse=new APIResponse<>("Retrived all users",users);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> getById(@PathVariable int id) {
        UserDTO user = userService.getById(id);
        APIResponse<UserDTO> apiResponse = new APIResponse<>("User found", user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> updateUser(@PathVariable int id, @RequestBody UserDTO dto) {
        UserDTO updated = userService.updateUser(id, dto);
        APIResponse<UserDTO> apiResponse=new APIResponse<>("User updated successfully", updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable int id) {
        String resultMessage = userService.deleteUser(id);
        APIResponse<String> apiResponse = new APIResponse<>(resultMessage, null);
        HttpStatus status = resultMessage.contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.OK;

        return new ResponseEntity<>(apiResponse, status);
    }
}
