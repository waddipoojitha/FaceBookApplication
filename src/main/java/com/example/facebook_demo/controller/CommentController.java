package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.CommentDTO;
import com.example.facebook_demo.DTO.CommentRequestDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.CommentService;

@RestController
@RequestMapping("/api/comments") 
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<APIResponse<CommentDTO>> create(@RequestBody CommentRequestDTO dto){
        CommentDTO comment=commentService.create(dto);
        APIResponse<CommentDTO> apiResponse=new APIResponse<>("Comment created successfully",comment);
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    } 

    @GetMapping
    public ResponseEntity<APIResponse<Page<CommentDTO>>> getAll(){
        Page<CommentDTO> comments=commentService.getAll();
        APIResponse<Page<CommentDTO>> apiResponse = new APIResponse<>("All comments retrieved successfully", comments);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDTO>> getCommentById(@PathVariable int id){
        CommentDTO comment=commentService.getCommentById(id);
        APIResponse<CommentDTO> apiResponse=new APIResponse<>("Comment found",comment);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<APIResponse<List<CommentDTO>>> getCommentsByPost(@PathVariable int postId){
        List<CommentDTO> comments=commentService.getCommentsByParent(postId,"POST");
        if (comments.isEmpty()) {
            APIResponse<List<CommentDTO>> apiResponse = new APIResponse<>("No comments exist for this post ", comments);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }
        APIResponse<List<CommentDTO>> apiResponse = new APIResponse<>("All comments for post retrieved successfully", comments);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<APIResponse<List<CommentDTO>>> getcommentsByComment(@PathVariable int commentId){
        List<CommentDTO> comments=commentService.getCommentsByParent(commentId,"COMMENT");
        if (comments.isEmpty()) {
            APIResponse<List<CommentDTO>> apiResponse = new APIResponse<>("No comments exist for this Comment ", comments);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }
        APIResponse<List<CommentDTO>> apiResponse = new APIResponse<>("All comments for comment retrieved successfully", comments);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
     
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteComment(@PathVariable int id){
        commentService.deleteComment(id);
        APIResponse<String> apiResponse = new APIResponse<>("Comment deleted successfully", "Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDTO>> updateComment(@PathVariable int id,@RequestParam String updatedComment){
        CommentDTO comment=commentService.updateComment(id,updatedComment);
        APIResponse<CommentDTO> apiResponse=new APIResponse<>("Comment updated successfully",comment);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}