package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.ReactionTypeDTO;
import com.example.facebook_demo.entity.ReactionType;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.ReactionTypeRepository;

@Service
public class ReactionTypeService {
    @Autowired
    private ReactionTypeRepository reactionTypeRepo;

    public ReactionTypeDTO saveReactionType(ReactionTypeDTO reactionTypeDTO){
        if(reactionTypeRepo.existsByType(reactionTypeDTO.getType())){
        throw new RuntimeException("Reaction type already exists");
    }

    ReactionType reactionType = new ReactionType();
    reactionType.setType(reactionTypeDTO.getType());
    reactionType.setCreatedAt(LocalDateTime.now());

    ReactionType saved = reactionTypeRepo.save(reactionType);
    return mapToDTO(saved);
    }

    private ReactionTypeDTO mapToDTO(ReactionType reactionType) {
        return new ReactionTypeDTO(
            reactionType.getId(),
            reactionType.getType());
    }

    public List<ReactionTypeDTO> getAll() {
        List<ReactionType> list= reactionTypeRepo.findAll();
        return list.stream().map(rt->new ReactionTypeDTO(rt.getId(),rt.getType())).collect(Collectors.toList());
    }

    public ReactionTypeDTO updateReactionType(int id, ReactionTypeDTO dto) {
        ReactionType reactionType=reactionTypeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        reactionType.setType(dto.getType());
        reactionType.setUpdatedAt(LocalDateTime.now());
        ReactionType updated=reactionTypeRepo.save(reactionType);
        return mapToDTO(updated);
    }

    public void deleteReactionType(int id){
        ReactionType reactionType=reactionTypeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        reactionTypeRepo.delete(reactionType);
    }
}