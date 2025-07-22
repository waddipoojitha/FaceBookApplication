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

import com.example.facebook_demo.DTO.ReactionTypeDTO;
import com.example.facebook_demo.service.ReactionTypeService;

@RestController
@RequestMapping("/api/reaction-types")
public class ReactionTypeController {
    @Autowired
    private ReactionTypeService reactionTypeService;

    @PostMapping
    public ResponseEntity<ReactionTypeDTO> saveReactionType(@RequestBody ReactionTypeDTO reactionTypeDTO)
    {
        ReactionTypeDTO savedReaction = reactionTypeService.saveReactionType(reactionTypeDTO);
        return new ResponseEntity<>(savedReaction, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ReactionTypeDTO>> getAll(){
        List<ReactionTypeDTO> types=reactionTypeService.getAll();
        return new ResponseEntity<>(types,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactionTypeDTO> updateReactionType(@PathVariable int id,@RequestBody ReactionTypeDTO dto){
        ReactionTypeDTO updatedReaction = reactionTypeService.updateReactionType(id, dto);
        return new ResponseEntity<>(updatedReaction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReactionType(@PathVariable int id){
        reactionTypeService.deleteReactionType(id);
        return new ResponseEntity<>("Reaction type deleted", HttpStatus.NO_CONTENT);
    }
} 
