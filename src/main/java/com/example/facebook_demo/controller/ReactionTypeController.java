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
import com.example.facebook_demo.DTO.ReactionTypeRequestDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.ReactionTypeService;

@RestController
@RequestMapping("/api/reaction-types")
public class ReactionTypeController {
    @Autowired
    private ReactionTypeService reactionTypeService;

    @PostMapping
    public ResponseEntity<APIResponse<ReactionTypeDTO>> saveReactionType(@RequestBody ReactionTypeRequestDTO reactionTypeDTO)
    {
        ReactionTypeDTO savedReaction = reactionTypeService.saveReactionType(reactionTypeDTO);
        APIResponse<ReactionTypeDTO> apiResponse=new APIResponse<>("Reaction type created",savedReaction);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<APIResponse<List<ReactionTypeDTO>>> getAll(){
        List<ReactionTypeDTO> types=reactionTypeService.getAll();
        APIResponse<List<ReactionTypeDTO>> apiResponse=new APIResponse<>("Retrived all reaction types",types);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ReactionTypeDTO>> updateReactionType(@PathVariable int id,@RequestBody ReactionTypeRequestDTO dto){
        ReactionTypeDTO updatedReaction = reactionTypeService.updateReactionType(id, dto);
        APIResponse<ReactionTypeDTO> apiResponse=new APIResponse<>("Updated reaction type",updatedReaction);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteReactionType(@PathVariable int id){
        reactionTypeService.deleteReactionType(id);
        APIResponse<String> apiResponse=new APIResponse<>("Reaction type deleted successfully","Deleted");
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
} 
