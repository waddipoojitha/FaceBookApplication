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

import com.example.facebook_demo.DTO.ReactionDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.ReactionService;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public ResponseEntity<APIResponse<ReactionDTO>> create (@RequestBody ReactionDTO dto){
        ReactionDTO reaction=reactionService.create(dto);
        APIResponse<ReactionDTO> apiResponse=new APIResponse<>("Reaction created successfully",reaction);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<ReactionDTO>>> getAll(){
        List<ReactionDTO> reactions = reactionService.getAll();
        APIResponse<List<ReactionDTO>> apiResponse=new APIResponse<>("Retrived all reactions",reactions);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ReactionDTO>> getReactionById(@PathVariable int id){
        ReactionDTO reaction=reactionService.getReactionById(id);
        APIResponse<ReactionDTO> apiResponse=new APIResponse<>("Retrived reaction",reaction);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<APIResponse<List<ReactionDTO>>> getReactionsByPost(@PathVariable int postId){
        List<ReactionDTO> reactions = reactionService.getReactionsByParent(postId, "POST");
        APIResponse<List<ReactionDTO>> apiResponse=new APIResponse<>("Retrived reactions of post",reactions);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}") 
    public ResponseEntity<APIResponse<List<ReactionDTO>>> getReactionByComment(@PathVariable int commentId){
        List<ReactionDTO> reactions = reactionService.getReactionsByParent(commentId, "COMMENT");
        APIResponse<List<ReactionDTO>> apiResponse=new APIResponse<>("Retrived reactions of comment",reactions);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteReaction(@PathVariable int id){
        reactionService.delete(id);
        APIResponse<String> apiResponse=new APIResponse<>("Reaction deleted","Deleted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT); 
    }
    @PutMapping("{id}")
    public ResponseEntity<APIResponse<ReactionDTO>> updatedReaction(@PathVariable int id,@RequestBody ReactionDTO dto){
        ReactionDTO updated = reactionService.updatedReaction(id, dto);
        APIResponse<ReactionDTO> apiResponse=new APIResponse<>("Updated reaction",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
