package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.Comment;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.PostRepository;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), commentDTO(post.getComments(),user, post.getId()));
    }

    private List<CommentOutputDTO> commentDTO(List<Comment> comments, User user, String post_id) {
        if (comments==null || comments.isEmpty()){
            return new ArrayList<>();
        }

        return comments.stream()
                .map(comment -> new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at()))
                .collect(Collectors.toList());
    }

    public List<PostOutputDTO> getAllPosts() {
        User user = new User();
        Post posts = new Post();

        return postRepo.findAll()
                .stream()
                .map(post -> new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(),user.getEmail(), post.getCreated_at(), commentDTO(post.getComments(), user, posts.getId())))
                .collect(Collectors.toList());
    }
}
