package com.example.facebook_demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.facebook_demo.DTO.GroupDTO;
import com.example.facebook_demo.entity.Group;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.typeMap(Group.class, GroupDTO.class).addMappings(m -> 
            m.map(src -> src.getCreatedBy().getId(), GroupDTO::setCreatedBy)
        );

        return modelMapper;
    }
}