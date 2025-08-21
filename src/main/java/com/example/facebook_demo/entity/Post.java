package com.example.facebook_demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    private String content;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at") 
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<PostMedia> media;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<GroupPost> groupPosts;

    public Post() {
    }

    public Post(User user, String content) {
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
