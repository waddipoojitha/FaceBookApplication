package com.example.facebook_demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(nullable = false,unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="profile_pic_url") 
    private String profilePicUrl;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="email_verified",nullable=false)
    private boolean emailVerified;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "createdBy",cascade = CascadeType.ALL)
    private List<Group> groups;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<GroupPost> groupPosts;

    public User() {
    }

    public User(String username, String firstName, String lastName, String email, String password, String profilePicUrl,LocalDate dateOfBirth) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
        this.createdAt = LocalDateTime.now();
        this.dateOfBirth=dateOfBirth;
        this.emailVerified=false;
    }
}
