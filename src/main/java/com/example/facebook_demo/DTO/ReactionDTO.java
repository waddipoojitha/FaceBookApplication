package com.example.facebook_demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {
    private int id;
    private int userId;
    private int reactionTypeId;
    private int parentId;
    private String parentType;
}
