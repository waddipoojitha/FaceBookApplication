package com.example.facebook_demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
            "cloud_name", "dup9yfo0t",
            "api_key", "467662715789297",
            "api_secret", "BPcDc0UfEcz51yzH3MhLELP5iIU"
        );
        return new Cloudinary(config);
    }
}
