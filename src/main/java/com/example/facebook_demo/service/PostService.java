package com.example.facebook_demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.facebook_demo.DTO.PostDTO;
import com.example.facebook_demo.DTO.PostMediaDTO;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.PostMedia;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.PostMediaRepository;
import com.example.facebook_demo.repository.PostRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;
    @Autowired private PostMediaRepository postMediaRepo;

    @Autowired
    private Cloudinary cloudinary;

    public PostDTO createPost(int userId, String content, List<MultipartFile> mediaFiles) {
    User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Post post = new Post();
    post.setUser(user);
    post.setContent(content);
    post.setCreatedAt(LocalDateTime.now());
    Post savedPost = postRepo.save(post);

    List<PostMedia> mediaEntityList = new ArrayList<>();
    List<PostMediaDTO> mediaDTOList = new ArrayList<>();

    for (MultipartFile file : mediaFiles) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("secure_url").toString();
            String mediaType = file.getContentType();

            PostMedia media = new PostMedia();
            media.setPost(savedPost);
            media.setMediaUrl(url);
            media.setMediaType(mediaType);
            media.setCreatedAt(LocalDateTime.now());
            mediaEntityList.add(media);

            mediaDTOList.add(new PostMediaDTO(url, mediaType));

        } catch (IOException e) {
            throw new RuntimeException("Error uploading to Cloudinary", e);
        }
    }
        postMediaRepo.saveAll(mediaEntityList);

        PostDTO response = new PostDTO();
        response.setId(savedPost.getId());
        response.setUserId(userId);
        response.setContent(savedPost.getContent());
        response.setCreatedAt(savedPost.getCreatedAt());
        response.setMedia(mediaDTOList);

        return response;
    }

    private PostDTO mapToDTO(Post post) {
        List<PostMediaDTO> mediaDTOs = post.getMedia().stream().map(media -> new PostMediaDTO(media.getMediaUrl(),media.getMediaType())).collect(Collectors.toList());

        return new PostDTO(
            post.getId(),
            post.getUser().getId(),
            post.getContent(),
            mediaDTOs,
            post.getCreatedAt(),
            post.getUpdatedAt(),
            post.getDeletedAt()     
        );
    }

    public List<PostDTO> getAll() {
    return postRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PostDTO getById(int id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        return mapToDTO(post);
        
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
        return mapToDTO(updated);
    }
    public List<PostDTO> getPostsByUser(int userId) {
        List<Post> posts = postRepo.findAllByUserId(userId);
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
