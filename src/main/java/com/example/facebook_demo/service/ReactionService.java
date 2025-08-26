package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.ReactionDTO;
import com.example.facebook_demo.DTO.ReactionPostRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Comment;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.Reaction;
import com.example.facebook_demo.entity.ReactionType;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceAlreadyExistsException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
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

    @Autowired private PostRepository postRepo;
    @Autowired private CommentRepository commentRepo;
    @Autowired private ModelMapper modelMapper;
    @Autowired private NotificationService notificationService;

    public ReactionDTO create(ReactionPostRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        User receiver;
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReactionType type=reactionTypeRepo.findByIdAndDeletedAtIsNull(dto.getReactionTypeId()).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));
        if(dto.getParentType().equalsIgnoreCase("POST")){
            Post target=postRepo.findByIdAndDeletedAtIsNull(dto.getParentId()).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
            receiver=target.getUser();
        }
        else if(dto.getParentType().equalsIgnoreCase("COMMENT")){
            Comment target=commentRepo.findByIdAndDeletedAtIsNull(dto.getParentId()).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
            receiver=target.getUser();
        }
        else{
            throw new ResourceNotFoundException("Invalid parent type: " + dto.getParentType());
        }
        if (reactionRepo.existsByUserIdAndParentIdAndParentTypeAndDeletedAtIsNull(user.getId(), dto.getParentId(), dto.getParentType())) {
            throw new ResourceAlreadyExistsException("You already reacted to this " + dto.getParentType());
        }
        Reaction reaction=new Reaction(user,type,dto.getParentId(),dto.getParentType());
        reaction.setCreatedAt(LocalDateTime.now());
        Reaction saved=reactionRepo.save(reaction);
        if (user.getId()!=(receiver.getId())) {
            notificationService.createNotification(user, receiver, "REACTION", username+" reacted to your "+reaction.getParentType());
        }
        return modelMapper.map(saved,ReactionDTO.class);
    }

    public List<ReactionDTO> getAll() {
        return reactionRepo.findByDeletedAtIsNull().stream().map(reaction->modelMapper.map(reaction,ReactionDTO.class)).collect((Collectors.toList()));
    }

    public ReactionDTO getReactionById(int id) {
        Reaction reaction=reactionRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        return modelMapper.map(reaction,ReactionDTO.class);
    }

    public List<ReactionDTO> getReactionsByParent(int parentId, String parentType) {
        List<Reaction> reactions = reactionRepo.findByParentIdAndParentTypeAndDeletedAtIsNull(parentId, parentType);
        return reactions.stream().map(reaction->modelMapper.map(reaction,ReactionDTO.class)).collect(Collectors.toList());
    }   

    public void delete(int id) {
        String username=SecurityUtils.getCurrentUsername();
        Reaction reaction=reactionRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        if(!username.equals(reaction.getUser().getUsername())){throw new UnauthorizedActionException("you can't delete the reaction");}
        reaction.setDeletedAt(LocalDateTime.now());
        reactionRepo.save(reaction);
    }

    public ReactionDTO updateReaction(int id,ReactionPostRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        Reaction reaction=reactionRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Reaction not found"));
        if(!username.equals(reaction.getUser().getUsername())){throw new UnauthorizedActionException("you can't update the reaction");}
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        ReactionType reactionType=reactionTypeRepo.findByIdAndDeletedAtIsNull(dto.getReactionTypeId()).orElseThrow(()->new ResourceNotFoundException("Reaction type not found"));

        reaction.setReactionType(reactionType);
        reaction.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(reactionRepo.save(reaction),ReactionDTO.class);      
    }
}