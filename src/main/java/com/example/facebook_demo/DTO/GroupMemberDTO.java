package com.example.facebook_demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDTO {
    private int id;
    private int groupId;
    private int userId;
    private int roleId;
}
