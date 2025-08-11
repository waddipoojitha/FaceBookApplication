package com.example.facebook_demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaDTO {
    private String mediaUrl;
    private String mediaType;
}
