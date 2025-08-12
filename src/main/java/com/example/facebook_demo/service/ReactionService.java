package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.ReactionDTO;
import com.example.facebook_demo.DTO.ReactionPostRequestDTO;
import com.example.facebook_demo.entity.Reaction;
import com.example.facebook_demo.entity.ReactionType;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.CommentRepository;
import com.example.facebook_demo.repository.PostRepository;
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

    @Autowired PostRepository postRepo;
    @Autowired CommentRepository commentRepo;

    public ReactionDTO create(String username,ReactionPostRequestDTO dto) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user.getDeletedAt()!=null){throw new ResourceNotFoundException("User doesn't exist");}
        ReactionType type=reactionTypeRepo.findById(dto.getReactionTypeId()).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        switch (dto.getParentType().toLowerCase()) {
            case "post":
                if (!postRepo.existsById(dto.getParentId())) {
                    throw new ResourceNotFoundException("Post not found");
                }
                break;
            case "comment":
                if (!commentRepo.existsById(dto.getParentId())) {
                    throw new ResourceNotFoundException("Comment not found");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid parent type: " + dto.getParentType());
        }
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
            reaction.getParentType()
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
        reaction.setDeletedAt(LocalDateTime.now());
        reactionRepo.save(reaction);
    }

    public ReactionDTO updatedReaction(int id, String username,ReactionPostRequestDTO dto) {
        Reaction reaction=reactionRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user.getDeletedAt()!=null){throw new ResourceNotFoundException("User doesn't exist");}
        
        ReactionType reactionType=reactionTypeRepo.findById(dto.getReactionTypeId()).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));

        reaction.setReactionType(reactionType);
        reaction.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(reactionRepo.save(reaction));      
    }
}