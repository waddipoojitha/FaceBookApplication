package com.example.facebook_demo.DTO;

import java.time.LocalDateTime;

public class GroupPostDTO {
    private int id;
    private int groupId;
    private int userId;
    private int postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public GroupPostDTO() {
    }
    public GroupPostDTO(int id, int groupId, int userId, int postId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getGroupId() {
        return groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
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
    
}
