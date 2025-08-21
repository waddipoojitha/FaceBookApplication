package com.example.facebook_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.facebook_demo.DTO.ForgetPasswordDTO;
import com.example.facebook_demo.DTO.PasswordChangeRequestDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("api/password")
public class PasswordController {
    @Autowired UserService userService;
    @PutMapping("/change-password")
    @ResponseBody
    public ResponseEntity<APIResponse<String>> changePassword(@RequestBody PasswordChangeRequestDTO dto){
        userService.changePassword(dto);
        APIResponse<String> apiResponse=new APIResponse<>("Password updated successfully",null);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    @ResponseBody
    public ResponseEntity<APIResponse<String>> forgetPassword(@RequestBody ForgetPasswordDTO dto){
        userService.sendResetPasswordLink(dto);
        APIResponse<String> apiResponse=new APIResponse<>("Reset link sent to email",null);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,@RequestParam String newPassword,Model model) {

        try {
            userService.resetPassword(token, newPassword);
            model.addAttribute("message", "Password reset successful!");
            return "reset-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        }
    }
}