package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="group_members")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="group_id",nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="role_id",nullable = false)
    private GroupRole groupRole;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    public GroupMember() {
    }

    public GroupMember(Group group, User user, GroupRole groupRole) {
        this.group = group;
        this.user = user;
        this.groupRole = groupRole;
        this.createdAt=LocalDateTime.now();
    }
}
