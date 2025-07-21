package com.example.facebook_demo.DTO;

import java.time.LocalDateTime;

public class GroupDTO {
    private int id;
    private int createdBy;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public GroupDTO() {
    }
    public GroupDTO(int id, int createdBy, String displayName, String description, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.createdBy = createdBy;
        this.displayName = displayName;
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
    public int getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
