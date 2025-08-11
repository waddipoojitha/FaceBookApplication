package com.example.facebook_demo.DTO;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int id;
    private int userId;
    private String content;
    private List<PostMediaDTO> media;
}