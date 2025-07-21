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
        return ResponseEntity.ok(reactionTypeService.saveReactionType(reactionTypeDTO));
    }
    @GetMapping
    public ResponseEntity<List<ReactionTypeDTO>> getAll(){
        return ResponseEntity.ok(reactionTypeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactionTypeDTO> updateReactionType(@PathVariable int id,@RequestBody ReactionTypeDTO dto){
        return ResponseEntity.ok(reactionTypeService.updateReactionType(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReactionType(@PathVariable int id){
        reactionTypeService.deleteReactionType(id);
        return ResponseEntity.ok("Reaction type deleted");
    }
} 
