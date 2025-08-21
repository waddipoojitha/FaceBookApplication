package com.example.facebook_demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="group_roles")
public class GroupRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column( unique = true,nullable = false)
    private String role;

    private String description;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "groupRole",cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers;

    public GroupRole() {
    }

    public GroupRole(String role, String description) {
        this.role = role;
        this.description = description;
        this.createdAt=LocalDateTime.now();
    }
}
