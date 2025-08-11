package com.example.facebook_demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private int id;
    private int userId;
    private int parentId;
    private String parentType; 
    private String comment;
}