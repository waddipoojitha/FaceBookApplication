package com.example.facebook_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reaction_type_id")
    private ReactionType reactionType;

    @Column(name="parent_id")
    private int parentId;

    @Column(name="parent_type")
    private String parentType;

    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    public Reaction() {
    }

    public Reaction(User user, ReactionType reactionType, int parentId, String parentType) {
        this.user = user;
        this.reactionType = reactionType;
        this.parentId = parentId;
        this.parentType = parentType;
        this.createdAt=LocalDateTime.now();
    }
}
