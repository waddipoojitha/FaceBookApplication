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

import com.example.facebook_demo.DTO.ReactionDTO;
import com.example.facebook_demo.service.ReactionService;

@RestController
@RequestMapping("/api/reaction")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionDTO> create (@RequestBody ReactionDTO dto){
        return ResponseEntity.ok(reactionService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReactionDTO>> getAll(){
        return ResponseEntity.ok(reactionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionDTO> getReactionById(@PathVariable int id){
        return ResponseEntity.ok(reactionService.getReactionById(id));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReactionDTO>> getReactionsByPost(@PathVariable int postId){
        return ResponseEntity.ok(reactionService.getReactionsByParent(postId,"POST"));
    }

    @GetMapping("/comment/{commentId}") 
    public ResponseEntity<List<ReactionDTO>> getReactionByComment(@PathVariable int commentId){
        return ResponseEntity.ok(reactionService.getReactionsByParent(commentId,"COMMENT"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReaction(@PathVariable int id){
        reactionService.delete(id);
        return ResponseEntity.ok("Reaction deleted");
    }
    @PutMapping("{id}")
    public ResponseEntity<ReactionDTO> updatedReaction(@PathVariable int id,@RequestBody ReactionDTO dto){
        return ResponseEntity.ok(reactionService.updatedReaction(id,dto));
    }
}
