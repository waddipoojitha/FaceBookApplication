package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="group_posts")
public class GroupPost {
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
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    public GroupPost() {
    }

    public GroupPost(Group group, User user, Post post) {
        this.group = group;
        this.user = user;
        this.post = post;
        this.createdAt=LocalDateTime.now();
    }
}
