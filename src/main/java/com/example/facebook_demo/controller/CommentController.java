package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(commentService.create(dto));
    }
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> postCommentOnPost(@PathVariable int postId,@RequestBody CommentDTO dto){
        return ResponseEntity.ok(commentService.postCommentOnParent(postId,"POST",dto));
    }
    @PostMapping("/comment/{commentID}")
    public ResponseEntity<CommentDTO> postCommentOnComment(@PathVariable("commentID") int commentId,@RequestBody CommentDTO dto){
        return ResponseEntity.ok(commentService.postCommentOnParent(commentId,"COMMENT",dto));
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAll(){
        return ResponseEntity.ok(commentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable int id){
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable int postId){
        return ResponseEntity.ok(commentService.getCommentsByParent(postId,"POST"));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<CommentDTO>> getcommentsByComment(@PathVariable int commentId){
        return ResponseEntity.ok(commentService.getCommentsByParent(commentId,"COMMENT"));
    }
     
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id){
        commentService.deleteComment(id);
        return ResponseEntity.ok("comment deleted");
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int id,@RequestBody CommentDTO dto){
        return ResponseEntity.ok(commentService.updateComment(id,dto));
    }
}
