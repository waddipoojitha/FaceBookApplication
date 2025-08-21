package com.example.facebook_demo.service;

import java.io.IOException;
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
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Post;
import com.example.facebook_demo.entity.PostMedia;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.MediaUploadException;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.exception.UnauthorizedActionException;
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

    public PostDTO createPost( String content, List<MultipartFile> mediaFiles) {
        String username=SecurityUtils.getCurrentUsername();
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepo.save(post);

        List<PostMedia> mediaEntityList = new ArrayList<>();
        List<PostMediaDTO> mediaDTOList = new ArrayList<>();
        if (mediaFiles != null) {
            for (MultipartFile file : mediaFiles) {
                if (file.isEmpty()) continue;
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto"));
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
                    throw new MediaUploadException("Error uploading to Cloudinary", e);
                }
            }
            if (!mediaEntityList.isEmpty()) {
                postMediaRepo.saveAll(mediaEntityList);
            }
        }
        PostDTO response = new PostDTO();
        response.setId(savedPost.getId());
        response.setUserId(user.getId());
        response.setContent(savedPost.getContent());
        response.setMedia(mediaDTOList);

        return response;
    }

    private PostDTO mapToDTO(Post post) {
        List<PostMediaDTO> mediaDTOs = post.getMedia().stream().map(media -> new PostMediaDTO(media.getMediaUrl(),media.getMediaType())).collect(Collectors.toList());

        return new PostDTO(
            post.getId(),
            post.getUser().getId(),
            post.getContent(),
            mediaDTOs  
        );
    }

    public List<PostDTO> getAll() {
        return postRepo.findByDeletedAtIsNull().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PostDTO getById(int id) {
        Post post = postRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        return mapToDTO(post);
    }

    public void deletePost(int id) {
        String username=SecurityUtils.getCurrentUsername();
        Post post = postRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        if(!post.getUser().getUsername().equals(username)){throw new UnauthorizedActionException("You cannot delete someone else's post");}
        post.setDeletedAt(LocalDateTime.now());
        postRepo.save(post);
    }

    public PostDTO updatePost(int id,String content) {
        String username=SecurityUtils.getCurrentUsername();
        Post post = postRepo.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Post doesn't exist with id " + id));
        User user = userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user==post.getUser()){
            post.setContent(content);
            post.setUpdatedAt(LocalDateTime.now() );

            Post updated=postRepo.save(post);
            return mapToDTO(updated);
        }
        else{
            throw new UnauthorizedActionException("You cannot update someone else's post");
        }
    }

    // public List<PostDTO> getPostsByUser(String username) {
    //     User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    //     if(user.getDeletedAt()!=null){throw new ResourceNotFoundException("User doesn't exist");}
    //     List<Post> posts = postRepo.findAllByUserId(user.getId());
    //     return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    // }

    public List<PostDTO> getPostsByUser(int userId) {
        User user = userRepo.findByIdAndDeletedAtIsNull(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Post> posts = postRepo.findAllByUserIdAndDeletedAtIsNull(userId);
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}