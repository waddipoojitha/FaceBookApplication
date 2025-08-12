package com.example.facebook_demo.DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserRequestDTO {
    
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password; 
    private LocalDate dateOfBirth;
}
