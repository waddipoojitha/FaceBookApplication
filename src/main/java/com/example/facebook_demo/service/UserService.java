package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired 
    UserRepository userRepo;
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDTO signup(UserDTO userDTO){
        if (userRepo.existsByEmail(userDTO.getEmail()) ){
            throw new RuntimeException("Email already signed up");
        }
        User user=new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setProfilePicUrl(userDTO.getProfilePicUrl());
        user.setCreatedAt(LocalDateTime.now());
        User savedUser=userRepo.save(user);
        return mapToDTO(savedUser);
    }

    public UserDTO mapToDTO(User user){
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getProfilePicUrl(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getDeletedAt());
    }

    public String verify(UserDTO userDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(userDTO.getUsername());
            }
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }

        return "Invalid credentials";
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().filter(user -> user.getDeletedAt() == null).map(this::mapToDTO) .collect(Collectors.toList());
    }
    public UserDTO getById(int id){
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
        return mapToDTO(user);
    }
    public UserDTO updateUser(int id,UserDTO userDTO){
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setProfilePicUrl(userDTO.getProfilePicUrl());
        user.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(user);
    }
    public String deleteUser(int id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));

        user.setDeletedAt(LocalDateTime.now());
        user.setEmail(user.getEmail()+"_deleted");
        userRepo.save(user);
        return "User with ID " + id + " has been soft-deleted.";
    }
}