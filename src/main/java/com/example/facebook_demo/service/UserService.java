package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.InvalidCredentialsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired 
    UserRepository userRepo;

    public UserDTO signup(UserDTO userDTO){
        if(userRepo.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("Email already signed up");
        }

        User user=new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
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

    public UserDTO login(String email, String password) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));

        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Incorrect password for email: " + email);
        }

        return mapToDTO(user);
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
        userRepo.save(user);
        return "User with ID " + id + " has been soft-deleted.";
}

}


