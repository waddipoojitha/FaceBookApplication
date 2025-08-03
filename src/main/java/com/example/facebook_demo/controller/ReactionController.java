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
import com.example.facebook_demo.service.ReactionService;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionDTO> create (@RequestBody ReactionDTO dto){
        ReactionDTO reaction=reactionService.create(dto);
        return new ResponseEntity<>(reaction,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReactionDTO>> getAll(){
        List<ReactionDTO> reactions = reactionService.getAll();
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionDTO> getReactionById(@PathVariable int id){
        ReactionDTO reaction=reactionService.getReactionById(id);
        return new ResponseEntity<>(reaction,HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsByPost(@PathVariable int postId){
        List<ReactionDTO> reactions = reactionService.getReactionsByParent(postId, "POST");
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}") 
    public ResponseEntity<List<ReactionDTO>> getReactionByComment(@PathVariable int commentId){
        List<ReactionDTO> reactions = reactionService.getReactionsByParent(commentId, "COMMENT");
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReaction(@PathVariable int id){
        reactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }
    @PutMapping("{id}")
    public ResponseEntity<ReactionDTO> updatedReaction(@PathVariable int id,@RequestBody ReactionDTO dto){
        ReactionDTO updated = reactionService.updatedReaction(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
