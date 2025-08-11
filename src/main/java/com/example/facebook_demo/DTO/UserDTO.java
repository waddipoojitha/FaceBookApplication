package com.example.facebook_demo.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password; 
    private String profilePicUrl;
    private LocalDate dateOfBirth;

    public UserDTO() {}

    public UserDTO(int id, String username, String firstName, String lastName,
                   String email, String password, String profilePicUrl,LocalDate dateOfBirth) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
        this.dateOfBirth=dateOfBirth;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}
}
