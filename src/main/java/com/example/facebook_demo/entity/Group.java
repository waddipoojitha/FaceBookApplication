package com.example.facebook_demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="created_by",nullable = false)
    private User createdBy;

    @Column(name="display_name",unique = true,nullable = false)
    private String displayName;
    private String description;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    private List<GroupPost> groupPosts;

    public Group() {
    }

    public Group(User createdBy, String displayName, String description) {
        this.createdBy = createdBy;
        this.displayName = displayName;
        this.description = description;
        this.createdAt=LocalDateTime.now();
    }     
}
