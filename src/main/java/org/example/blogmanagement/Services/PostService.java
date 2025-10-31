package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.Comment;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepo;

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

        return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), commentDTO(post.getComments(),user));
    }

    private List<CommentOutputDTO> commentDTO(List<Comment> comments, User user) {
        if (comments==null || comments.isEmpty()){
            return new ArrayList<>();
        }

        return comments.stream()
                .map(comment -> new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at()))
                .collect(Collectors.toList());
    }

    public List<PostOutputDTO> getAllPosts(){
        return postRepo.findAll()
                .stream()
                .map(post -> {
                    // Looking for the user associated to a post
                    User user = userRepo.findById(post.getAuthor_id())
                            .orElseThrow(() -> new ResourceNotFound("This post is not attached to an author!"));

                    // Fetching all the comments connected to a post
                    List<Comment> comments = commentRepo.findByPostId(post.getId());

                    // Converting the comments to comment DTOs
                    List<CommentOutputDTO> commentDTOs = commentDTO(comments, user);

                    // Returning the final PostOutputDTO

                    return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), commentDTOs);
                })
                .collect(Collectors.toList());
    }

}

