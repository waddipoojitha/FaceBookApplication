package com.example.facebook_demo.DTO;

import java.time.LocalDateTime;

public class GroupMemberDTO {
    private int id;
    private int groupId;
    private int userId;
    private int roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public GroupMemberDTO(int id, int groupId, int userId, int roleId, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public GroupMemberDTO() {
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
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
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
