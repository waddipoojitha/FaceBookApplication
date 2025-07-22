package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.CommentDTO;
import com.example.facebook_demo.service.CommentService;

@RestController
@RequestMapping("/api/comment") 
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody CommentDTO dto){
        CommentDTO comment=commentService.create(dto);
        return new ResponseEntity<>(comment,HttpStatus.CREATED);
    }
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> postCommentOnPost(@PathVariable int postId,@RequestBody CommentDTO dto){
        CommentDTO comment=commentService.postCommentOnParent(postId,"POST",dto);
        return new ResponseEntity<>(comment,HttpStatus.CREATED);
    }
    @PostMapping("/comment/{commentID}")
    public ResponseEntity<CommentDTO> postCommentOnComment(@PathVariable("commentID") int commentId,@RequestBody CommentDTO dto){
        CommentDTO comment=commentService.postCommentOnParent(commentId,"COMMENT",dto);
        return new ResponseEntity<>(comment,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAll(){
        List<CommentDTO> comments=commentService.getAll();
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable int id){
        CommentDTO comment=commentService.getCommentById(id);
        return new ResponseEntity<>(comment,HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable int postId){
        List<CommentDTO> comments=commentService.getCommentsByParent(postId,"POST");
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<CommentDTO>> getcommentsByComment(@PathVariable int commentId){
        List<CommentDTO> comments=commentService.getCommentsByParent(commentId,"POST");
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }
     
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id){
        commentService.deleteComment(id);
        return new ResponseEntity<>("comment deleted",HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int id,@RequestBody CommentDTO dto){
        CommentDTO comment=commentService.updateComment(id,dto);
        return new ResponseEntity<>(comment,HttpStatus.OK);
    }
}