package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.ReactionDTO;
import com.example.facebook_demo.entity.Reaction;
import com.example.facebook_demo.entity.ReactionType;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.InvalidCredentialsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.ReactionRepository;
import com.example.facebook_demo.repository.ReactionTypeRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ReactionTypeRepository reactionTypeRepo;

    public ReactionDTO create(ReactionDTO dto) {
        User user=userRepo.findById(dto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User doesn't exists"));
        ReactionType type=reactionTypeRepo.findById(dto.getReactionTypeId()).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        Reaction reaction=new Reaction(user,type,dto.getParentId(),dto.getParentType());
        reaction.setCreatedAt(LocalDateTime.now());
        Reaction saved=reactionRepo.save(reaction);
        return mapToDTO(saved);
    }

    private ReactionDTO mapToDTO(Reaction reaction) {
        return new ReactionDTO(
            reaction.getId(),
            reaction.getUser().getId(),
            reaction.getReactionType().getId(),
            reaction.getParentId(),
            reaction.getParentType(),
            reaction.getCreatedAt(),
            reaction.getUpdatedAt(),
            reaction.getDeletedAt()
        );
    }

    public List<ReactionDTO> getAll() {
        return reactionRepo.findAll().stream().map(this::mapToDTO).collect((Collectors.toList()));
    }

    public ReactionDTO getReactionById(int id) {
        Reaction reaction=reactionRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        return mapToDTO(reaction);
    }

    public List<ReactionDTO> getReactionsByParent(int parentId, String parentType) {
        List<Reaction> reactions = reactionRepo.findByParentIdAndParentType(parentId, parentType);
        return reactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }   


    public void delete(int id) {
        Reaction reaction=reactionRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        reactionRepo.delete(reaction);
    }

    public ReactionDTO updatedReaction(int id, ReactionDTO dto) {
        Reaction reaction=reactionRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        if(reaction.getUser().getId()!=dto.getUserId()){
            throw new InvalidCredentialsException("Same user can modify the the reaction");
        }
        ReactionType reactionType=reactionTypeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));

        reaction.setReactionType(reactionType);
        reaction.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(reaction);      
    }

}
