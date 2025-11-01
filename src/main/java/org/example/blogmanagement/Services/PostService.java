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

    @Autowired
    private CommentService commentService;

    private List<CommentOutputDTO> commentDTO(List<Comment> comments) {
        if (comments==null || comments.isEmpty()){
            return new ArrayList<>();
        }

        return comments.stream()
                .map(comment -> {
                    User commentAuthor = userRepo.findById(comment.getAuthor_id())
                            .orElseThrow(() -> new ResourceNotFound("User with id '" + comment.getAuthor_id() + "' was not found"));

                    return new CommentOutputDTO(comment.getContent(), commentAuthor.getUsername(), commentAuthor.getEmail(), comment.getCreated_at());
                })
                .collect(Collectors.toList());
    }

    private PostOutputDTO post(Post post){

        User user = userRepo.findById(post.getAuthor_id())
                .orElseThrow(() -> new ResourceNotFound("This post is not attached to an author!"));

        List<Comment> comments = commentRepo.findByPostId(post.getId());

        List<CommentOutputDTO> commentDTOs = commentDTO(comments);

        return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), post.getUpdated_at(), commentDTOs);
    }

    public PostOutputDTO createPost(PostInputDTO postInputDTO) {
        // Find the user by email from PostgresSQL

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

        return post(post);
    }

    public List<PostOutputDTO> getAllPosts(){
        return postRepo.findAll()
                .stream()
                .map(this::post)
                .collect(Collectors.toList());
    }

    public PostOutputDTO getPostById(String postId) {
        return postRepo.findById(postId)
                .map(this::post)
                .orElseThrow(() -> new ResourceNotFound("Post with id '" + postId + "' was not found"));
    }

    public PostOutputDTO updatePost(String postId, PostInputDTO postInputDTO) {
        return postRepo.findById(postId)
                .map(post -> {
                    post.setTitle(postInputDTO.getTitle());
                    post.setContent(postInputDTO.getContent());
                    post.setUpdated_at(LocalDateTime.now());
                    postRepo.save(post);

                    return post(post);
                }).orElseThrow(() -> new ResourceNotFound("Post with id '" + postId + "' was not found"));
    }

    public void deletePost(String postId) {

        if(postRepo.existsById(postId)){
            commentService.deleteCommentByPostId(postId);
            postRepo.deleteById(postId);

        } else {
            throw new ResourceNotFound("Post with id '" + postId + "' was not found");
        }

    }
}

