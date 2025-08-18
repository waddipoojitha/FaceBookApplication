package com.example.facebook_demo.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.facebook_demo.DTO.LoginDTO;
import com.example.facebook_demo.DTO.RefreshTokenRequestDTO;
import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.DTO.UserRequestDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.JwtService;
import com.example.facebook_demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDTO>> signup(@RequestBody UserRequestDTO userDTO) {
        UserDTO registeredUser = userService.signup(userDTO);

        APIResponse<UserDTO> apiResponse = new APIResponse<>("User registered succcessfully",registeredUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<Map<String,String>>> login(@RequestBody LoginDTO loginDTO) {
        try{
            Map<String,String> tokens=userService.verify(loginDTO);
            APIResponse<Map<String,String>> apiResponse = new APIResponse<>("User logged in successfully", tokens);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch(RuntimeException ex){
            APIResponse<Map<String,String>> apiResponse=new APIResponse<>(ex.getMessage(),null);
            return new ResponseEntity<>(apiResponse,HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value = "/add-profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<UserDTO>> uploadProfilePic(@RequestPart("profile_pic") MultipartFile profilePic,Principal principal) {

        UserDTO updatedUser = userService.updateProfilePic(principal.getName(), profilePic);
        APIResponse<UserDTO> apiResponse = new APIResponse<>("Profile picture updated successfully", updatedUser);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @Operation(summary = "Get all users",description = "Fetches all users from the DB")
    @GetMapping
    public ResponseEntity<APIResponse<Page<UserDTO>>> getAll() {
        Page<UserDTO> users = userService.getAllUsers();
        APIResponse<Page<UserDTO>> apiResponse=new APIResponse<>("Retrived all users",users);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> getById(@PathVariable int id) {
        UserDTO user = userService.getById(id);
        APIResponse<UserDTO> apiResponse = new APIResponse<>("User found", user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<APIResponse<UserDTO>> updateUser( @RequestBody UserRequestDTO dto,Principal principal) {
        UserDTO updated = userService.updateUser(principal.getName(), dto);
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

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<String>> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();

        try {
            String tokenType = jwtService.extractTokenType(refreshToken);
            if (!"refresh".equals(tokenType)) {
                return new ResponseEntity<>(new APIResponse<>("Invalid token type", null), HttpStatus.UNAUTHORIZED);
            }
            String username = jwtService.extractUserName(refreshToken);

            if (!jwtService.isTokenExpired(refreshToken)) {
                String newAccessToken = jwtService.generateAccessToken(username);
                APIResponse<String> apiResponse = new APIResponse<>("New access token generated", newAccessToken);
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new APIResponse<>("Refresh token expired", null), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>("Invalid refresh token", null), HttpStatus.UNAUTHORIZED);
        }
    }
}