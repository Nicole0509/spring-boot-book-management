package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService (PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostOutputDTO createPost(PostInputDTO postInputDTO) {
        Post post  = new Post();

        post.setTitle(postInputDTO.getTitle());
        post.setContent(postInputDTO.getContent());
        post.setAuthor_id(2L);
        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());

        postRepository.save(post);

        return new PostOutputDTO(post.getTitle(), post.getContent(), post.getUpdated_at(), post.getComments());
    }
}
