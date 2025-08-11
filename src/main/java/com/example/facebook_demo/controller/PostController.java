package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.facebook_demo.DTO.PostDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/posts") 
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<PostDTO>> createPost(@RequestParam("userId") int userId,@RequestParam("content") String content,@RequestParam("media") List<MultipartFile> media){
        PostDTO created = postService.createPost(userId,content,media);
        APIResponse<PostDTO> apiResponse=new APIResponse<>("Post created successfully",created);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<PostDTO>>> getAll(){
        List<PostDTO> posts = postService.getAll();
        APIResponse<List<PostDTO>> apiResponse=new APIResponse<>("Retrived all posts",posts);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PostDTO>> getById(@PathVariable int id){
        PostDTO post = postService.getById(id);
        APIResponse<PostDTO> apiResponse=new APIResponse<>("Retrived post",post);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse<List<PostDTO>>> getPostsByUser(@PathVariable int userId){
        List<PostDTO> posts = postService.getPostsByUser(userId);
        APIResponse<List<PostDTO>> apiResponse=new APIResponse<>("Retrived all posts of user",posts);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deletePost(@PathVariable int id){
        postService.deletePost(id);
        APIResponse<String> apiResponse=new APIResponse<>("Deleted post","Delted");
        return new ResponseEntity<>(apiResponse,HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<PostDTO>> updatePost(@PathVariable int id,@RequestBody PostDTO dto){
        PostDTO updated = postService.updatePost(id, dto);
        APIResponse<PostDTO> apiResponse=new APIResponse<>("Updated post successfully",updated);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // @GetMapping("/csrf-token")
    // public CsrfToken getCsrf(HttpServletRequest request){
    //     return (CsrfToken) request.getAttribute("_csrf");
    // }

}