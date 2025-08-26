package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.CommentDTO;
import com.example.facebook_demo.DTO.CommentRequestDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Comment;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
import com.example.facebook_demo.repository.CommentRepository;
import com.example.facebook_demo.repository.PostRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired private PostRepository postRepo;
    @Autowired private ModelMapper modelMapper;
    @Autowired private NotificationService notificationService;


    public CommentDTO create(CommentRequestDTO dto) {
        String username=SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User receiver;
        if(dto.getParentType().equalsIgnoreCase("POST")){
            Post target = postRepo.findByIdAndDeletedAtIsNull(dto.getParentId()).orElseThrow(()->new ResourceNotFoundException("Post not found"));
            receiver=target.getUser();
        }
        else if(dto.getParentType().equalsIgnoreCase("COMMENT")){
            Comment target = commentRepo.findByIdAndDeletedAtIsNull(dto.getParentId()).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
            receiver=target.getUser();
        }
        else{
            throw new ResourceNotFoundException("Invalid Parent Type: "+dto.getParentType());
        }

        Comment comment=modelMapper.map(dto,Comment.class);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        Comment saved=commentRepo.save(comment);
        if (user.getId()!=(receiver.getId())) {
            notificationService.createNotification(user, receiver, "COMMENT", username+" commented on your "+comment.getParentType());
        }
        return modelMapper.map(saved,CommentDTO.class);
    }

    public Page<CommentDTO> getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        return commentRepo.findByDeletedAtIsNull(pageable).map(comment->modelMapper.map(comment,CommentDTO.class));
    }

    public CommentDTO getCommentById(int id){
        Comment comment=commentRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        return modelMapper.map(comment,CommentDTO.class);
    }

    public List<CommentDTO> getCommentsByParent(int parentId, String parentType) {

        if(parentType.equalsIgnoreCase("POST")){
            boolean exists = postRepo.existsByIdAndDeletedAtIsNull(parentId);
            if (!exists) {
                throw new ResourceNotFoundException("Post with id " + parentId + " not found");
            }
        }
        else if(parentType.equalsIgnoreCase("COMMENT")){
            boolean exists = commentRepo.existsByIdAndDeletedAtIsNull(parentId);
            if (!exists) {
                throw new ResourceNotFoundException("Comment with id " + parentId + " not found");
            }
        }

        return commentRepo.findByParentIdAndParentTypeAndDeletedAtIsNull(parentId,parentType).stream().map(comment->modelMapper.map(comment,CommentDTO.class)).collect(Collectors.toList());
    }

    public void deleteComment(int id) {
        Comment comment=commentRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        String username=SecurityUtils.getCurrentUsername();
        if(!username.equals(comment.getUser().getUsername())){throw new UnauthorizedActionException("You can't delete this comment");}
        comment.setDeletedAt(LocalDateTime.now());
        commentRepo.save(comment);
    }

    public CommentDTO updateComment(int id, String updatedComment) {
        String username=SecurityUtils.getCurrentUsername();
        Comment comment=commentRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        if(!username.equals(comment.getUser().getUsername())){throw new UnauthorizedActionException("You can't update this comment");}
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        comment.setComment(updatedComment);
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updated=commentRepo.save(comment);
        return modelMapper.map(updated,CommentDTO.class);
    }
}