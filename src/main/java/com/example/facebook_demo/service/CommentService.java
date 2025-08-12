package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.CommentDTO;
import com.example.facebook_demo.entity.Comment;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
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


    public CommentDTO create(CommentDTO dto) {
        User user=userRepo.findById(dto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not exists"));
        Comment comment=new Comment();
        comment.setUser(user);
        comment.setParentId((dto.getParentId()));
        comment.setParentType(dto.getParentType());
        comment.setComment(dto.getComment());
        comment.setCreatedAt(LocalDateTime.now());

        return mapToDTO(commentRepo.save(comment));
    }

    private CommentDTO mapToDTO(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getUser().getId(),
            comment.getParentId(),
            comment.getParentType(),
            comment.getComment()
        );
    }

    public List<CommentDTO> getAll() {
        return commentRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CommentDTO getCommentById(int id){
        Comment comment=commentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("comment not found"));
        return mapToDTO(comment);
    }

    public List<CommentDTO> getCommentsByParent(int parentId, String parentType) {
        return commentRepo.findByParentIdAndParentType(parentId,parentType).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void deleteComment(int id) {
        Comment comment=commentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        commentRepo.delete(comment);
    } 

    public CommentDTO updateComment(int id, CommentDTO dto) {
        Comment comment=commentRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        comment.setComment(dto.getComment());
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updated=commentRepo.save(comment);
        return mapToDTO(updated);
    }

}