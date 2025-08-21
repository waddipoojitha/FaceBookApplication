package com.example.facebook_demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="reaction_types")
public class ReactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false,unique = true)
    private String type;

    @Column(name="created_at",nullable = true)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "reactionType",cascade = CascadeType.ALL)
    private List<Reaction> reactions;

    public ReactionType() { 
    }

    public ReactionType(String type, LocalDateTime createdAt) {
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }
}
