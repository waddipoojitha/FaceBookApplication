package com.example.facebook_demo.DTO;

import java.time.LocalDateTime;

public class GroupRoleDTO {
    private int id;
    private String role;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public GroupRoleDTO() {
    }
    public GroupRoleDTO(int id, String role, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.role = role;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
