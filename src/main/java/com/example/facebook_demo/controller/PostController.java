package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.PostDTO;
import com.example.facebook_demo.service.PostService;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/post") 
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO dto){
        return ResponseEntity.ok(postService.createPost(dto));
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAll(){
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable int id){
        return ResponseEntity.ok(postService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable int userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id){
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable int id,@RequestBody PostDTO dto){
        return ResponseEntity.ok(postService.updatePost(id,dto));        
    }

}
