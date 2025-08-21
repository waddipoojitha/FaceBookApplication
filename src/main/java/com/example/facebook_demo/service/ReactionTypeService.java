package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.ReactionTypeDTO;
import com.example.facebook_demo.DTO.ReactionTypeRequestDTO;
import com.example.facebook_demo.entity.ReactionType;
import com.example.facebook_demo.exception.ResourceAlreadyExistsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.ReactionTypeRepository;

@Service
public class ReactionTypeService {
    @Autowired
    private ReactionTypeRepository reactionTypeRepo;

    @Autowired private ModelMapper modelMapper;

    public ReactionTypeDTO saveReactionType(ReactionTypeRequestDTO reactionTypeDTO){
        if(reactionTypeRepo.existsByType(reactionTypeDTO.getType())){
            throw new ResourceAlreadyExistsException("Reaction type already exists");
        }

        ReactionType reactionType=modelMapper.map(reactionTypeDTO,ReactionType.class);
        reactionType.setCreatedAt(LocalDateTime.now());

        ReactionType saved = reactionTypeRepo.save(reactionType);
        return modelMapper.map(saved,ReactionTypeDTO.class);
    }

    public List<ReactionTypeDTO> getAll() {
        List<ReactionType> list= reactionTypeRepo.findByDeletedAtIsNull();
        return list.stream().map(rt->modelMapper.map(rt,ReactionTypeDTO.class)).collect(Collectors.toList());
    }

    public ReactionTypeDTO updateReactionType(int id, ReactionTypeRequestDTO dto) {
        ReactionType reactionType=reactionTypeRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        reactionType.setType(dto.getType());
        reactionType.setUpdatedAt(LocalDateTime.now());
        ReactionType updated=reactionTypeRepo.save(reactionType);
        return modelMapper.map(updated,ReactionTypeDTO.class);
    }

    public void deleteReactionType(int id){
        ReactionType reactionType=reactionTypeRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        reactionType.setDeletedAt(LocalDateTime.now());
        reactionTypeRepo.save(reactionType);
    }
}