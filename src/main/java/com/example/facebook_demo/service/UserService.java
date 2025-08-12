package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.LoginDTO;
import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class UserService {
    @Autowired 
    private UserRepository userRepo;
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;
    @Autowired private SendEmailService sendEmailService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDTO signup(UserDTO userDTO){
        if (userRepo.existsByEmail(userDTO.getEmail())) {
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
        user.setDateOfBirth(userDTO.getDateOfBirth());

        User savedUser=userRepo.save(user);
        try {
            sendEmailService.sendSignupEmail(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mapToDTO(savedUser);
    }

    public Map<String,String> verify(LoginDTO loginDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                // return jwtService.generateToken(loginDTO.getUsername());
                String accessToken=jwtService.generateAccessToken(loginDTO.getUsername());
                String refreshToken=jwtService.generateRefreshToken(loginDTO.getUsername());

                Map<String,String> tokens=new HashMap<>();
                tokens.put("accesstoken", accessToken);
                tokens.put("refreshToken", refreshToken);
                return tokens;
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }

        throw new RuntimeException("Invalid credentials");
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().filter(user -> user.getDeletedAt() == null).map(this::mapToDTO) .collect(Collectors.toList());
    }

    public UserDTO getById(int id){
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if (user.getDeletedAt() != null) {throw new ResourceNotFoundException("User with ID " + id + " not found");}
        return mapToDTO(user);
    }

    public UserDTO updateUser(String username,UserDTO userDTO){
        User user=userRepo.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        if (user.getDeletedAt() != null) {throw new ResourceNotFoundException("User not found");}
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getFirstName() != null) { 
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getProfilePicUrl() != null) {
            user.setProfilePicUrl(userDTO.getProfilePicUrl());
        }
        if (userDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(userDTO.getDateOfBirth());
        }
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepo.save(user);
        return mapToDTO(updatedUser);
    }

    public String deleteUser(int id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        if(user.getDeletedAt()==null){
            user.setDeletedAt(LocalDateTime.now());
            user.setEmail(user.getEmail()+"_deleted");
            userRepo.save(user);
            return "User with ID " + id + " has been soft-deleted.";
        }
        else{
            return "User with ID " + id + " not found ";
        }
    }

    public UserDTO mapToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getProfilePicUrl(),
            user.getDateOfBirth()
        );
    }
}