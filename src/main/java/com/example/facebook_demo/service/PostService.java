package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.PostDTO;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.PostRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    public PostDTO createPost(PostDTO dto) {
        Post post=new Post();
        post.setContent(dto.getContent());
        post.setCreatedAt(LocalDateTime.now());

        User user=userRepo.findById(dto.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not found with id "+dto.getUserId()));
        post.setUser(user);

        Post saved=postRepo.save(post);
        return maptoDTO(saved);
    }

    private PostDTO maptoDTO(Post post) {
        return new PostDTO(
            post.getId(),
            post.getUser().getId(),
            post.getContent(),
            post.getCreatedAt(),
            post.getUpdatedAt(),
            post.getDeletedAt()
        );
    }

    public List<PostDTO> getAll() {
        return postRepo.findAll().stream().map(post-> new PostDTO(post.getId(),post.getUser().getId(),post.getContent(),post.getCreatedAt(),post.getUpdatedAt(),post.getDeletedAt()))
        .collect(Collectors.toList());
    }

    public PostDTO getById(int id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        return maptoDTO(post);
        
    }

    public void deletePost(int id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        postRepo.delete(post);
    }

    public PostDTO updatePost(int id, PostDTO dto) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        post.setContent(dto.getContent());
        post.setUpdatedAt(LocalDateTime.now() );

        Post updated=postRepo.save(post);
        return maptoDTO(updated);
    }

    public List<PostDTO> getPostsByUser(int userId) {
        List<Post> posts=postRepo.findAllByUserId(userId);
        return posts.stream().map(post->new PostDTO(post.getId(),post.getUser().getId(),post.getContent(),post.getCreatedAt(),post.getUpdatedAt(),post.getDeletedAt()))
        .collect(Collectors.toList());
    }

}
