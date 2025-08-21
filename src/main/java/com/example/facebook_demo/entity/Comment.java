package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name = "parent_id",nullable = false)
    private int parentId;

    @Column(name="parent_type")
    private String parentType;

    @Column(nullable = false)
    private String comment;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt; 

    public Comment() {
    }

    public Comment(User user, int parentId, String parentType, String comment) {
        this.user = user;
        this.parentId = parentId;
        this.parentType = parentType;
        this.comment = comment;
        this.createdAt=LocalDateTime.now();
    }
}
