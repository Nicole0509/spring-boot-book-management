package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.PostRepository;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    public PostOutputDTO createPost(PostInputDTO postInputDTO) {
        // Find the user by email from PostgreSQL

        User user = userRepo.findByEmail(postInputDTO.getAuthor_email())
                .orElseThrow(() -> new ResourceNotFound("User with email '" + postInputDTO.getAuthor_email() + "' was not found"));

        // Create A Post
        Post post  = new Post();

        post.setTitle(postInputDTO.getTitle());
        post.setContent(postInputDTO.getContent());
        post.setAuthor_id(user.getId());
        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());

        postRepo.save(post);

        return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), post.getComments());
    }
}
