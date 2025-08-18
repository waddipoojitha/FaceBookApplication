package com.example.facebook_demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeRequestDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}