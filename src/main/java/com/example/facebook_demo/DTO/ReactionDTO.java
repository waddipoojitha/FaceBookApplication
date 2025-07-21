package com.example.facebook_demo.DTO;

import java.time.LocalDateTime;

public class ReactionDTO {
    private int id;
    private int userId;
    private int reactionTypeId;
    private int parentId;
    private String parentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    public ReactionDTO() {
    }
    public ReactionDTO(int id, int userId, int reactionTypeId, int parentId, String parentType, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.reactionTypeId = reactionTypeId;
        this.parentId = parentId;
        this.parentType = parentType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getReactionTypeId() {
        return reactionTypeId;
    }
    public void setReactionTypeId(int reactionTypeId) {
        this.reactionTypeId = reactionTypeId;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public String getParentType() {
        return parentType;
    }
    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    

}
