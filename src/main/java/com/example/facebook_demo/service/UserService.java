package com.example.facebook_demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.facebook_demo.DTO.ForgetPasswordDTO;
import com.example.facebook_demo.DTO.LoginDTO;
import com.example.facebook_demo.DTO.PasswordChangeRequestDTO;
import com.example.facebook_demo.DTO.UserDTO;
import com.example.facebook_demo.DTO.UserSignupRequestDTO;
import com.example.facebook_demo.DTO.UserUpdateRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.InvalidCredentialsException;
import com.example.facebook_demo.exception.InvalidTokenException;
import com.example.facebook_demo.exception.MediaUploadException;
import com.example.facebook_demo.exception.ResourceAlreadyExistsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
import com.example.facebook_demo.repository.UserRepository;

import io.jsonwebtoken.Claims;

import org.springframework.data.domain.Pageable; 

import jakarta.mail.MessagingException;

@Service
public class UserService {
    @Autowired 
    private UserRepository userRepo;
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;
    @Autowired private SendEmailService sendEmailService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private Cloudinary cloudinary;
    @Autowired private ModelMapper modelMapper;

    public UserDTO signup(UserSignupRequestDTO userDTO){
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already signed up");
        }
        User user=modelMapper.map(userDTO,User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setEmailVerified(false);

        User savedUser=userRepo.save(user);
        String token=jwtService.generateEmailVerificationToken(savedUser.getUsername());
        String verificationLink="http://localhost:8080/api/password/verify?token="+token;
        try {
            sendEmailService.sendSignupEmail(
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                verificationLink
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return modelMapper.map(savedUser,UserDTO.class);
    }

    public Map<String,String> login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                User user=userRepo.findByUsername(loginDTO.getUsername()).orElseThrow(()->new ResourceNotFoundException("User not found"));
                if(!user.isEmailVerified()){throw new InvalidCredentialsException("Email not verified");}

                String accessToken=jwtService.generateAccessToken(loginDTO.getUsername());
                String refreshToken=jwtService.generateRefreshToken(loginDTO.getUsername());

                Map<String,String> tokens=new HashMap<>();
                tokens.put("accesstoken", accessToken);
                tokens.put("refreshToken", refreshToken);
                return tokens;
            }
        } 
        catch (Exception e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        throw new InvalidCredentialsException("Invalid credentials");
    }

    public UserDTO updateProfilePic(MultipartFile profilePic) {
        String username=SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try{
            Map uploadResult = cloudinary.uploader().upload(profilePic.getBytes(),ObjectUtils.asMap("folder", "profile_pics"));

            String imageUrl = uploadResult.get("secure_url").toString();

            user.setProfilePicUrl(imageUrl);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);

            return modelMapper.map(user,UserDTO.class);
        } 
        catch (IOException e) {
            throw new MediaUploadException("Error uploading to Cloudinary", e);
        }
    }
    
    public Page<UserDTO> getAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        return userRepo.findByDeletedAtIsNull(pageable).map(user->modelMapper.map(user,UserDTO.class));
    }

    public UserDTO getById(int id){
        User user=userRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return modelMapper.map(user,UserDTO.class);
    }

    public UserDTO updateUser(UserUpdateRequestDTO userDTO){
        String username=SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        modelMapper.map(userDTO, user);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepo.save(user);
        return modelMapper.map(updatedUser,UserDTO.class);
    }

    public String deleteUser(int id) {
        User user = userRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        String username=SecurityUtils.getCurrentUsername();
        if(!username.equals(user.getUsername())){throw new UnauthorizedActionException("You can't delete this user account");}
        user.setDeletedAt(LocalDateTime.now());
        // user.setEmail(user.getEmail()+"_deleted");
        userRepo.save(user);
        // userRepo.delete(user);
        return "User with ID " + id + " has been soft-deleted.";
    }

    public User changePassword(PasswordChangeRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password and new password should not be same");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new InvalidCredentialsException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return userRepo.save(user);
    }

    public void sendResetPasswordLink(ForgetPasswordDTO dto) {
        User user=userRepo.findByEmailAndDeletedAtIsNull(dto.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        String token = jwtService.generatePasswordResetToken(user.getEmail(), 15);

        String resetLink = "http://localhost:8080/api/password/reset-password?token=" + token;
        sendEmailService.sendEmail(user.getEmail(), "Click here to reset your password: " + resetLink, "Password Reset");
    }

    public void resetPassword(String token, String newPassword) {
       Claims claims = jwtService.extractAllClaims(token);
    
        String type = claims.get("type", String.class);
        if (!"password_reset".equals(type)) {
            throw new InvalidTokenException("Invalid token type");
        }

        String email = claims.getSubject();
        User user = userRepo.findByEmailAndDeletedAtIsNull(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Old password and new password should not be same");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        // System.out.println("password reseted successfully");
    }

    public String verifyEmailToken(String token) {
        Claims claims = jwtService.extractAllClaims(token);

        if (!"email_verification".equals(claims.get("type"))) {
            return "Invalid token type";
        }

        String username = claims.getSubject();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isEmailVerified()) {
            return "Your email is already verified";
        }

        user.setEmailVerified(true);
        userRepo.save(user);
        return "Email verified successfully";
    }
    
}