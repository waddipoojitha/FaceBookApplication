package com.example.facebook_demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostRequestDTO {
    private int groupId;
    private int postId;
}
