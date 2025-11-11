package org.example.blogmanagement.Services;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private JWTService jwtService;

    private List<CommentOutputDTO> commentDTO(List<Comment> comments) {
        if (comments==null || comments.isEmpty()){
            return new ArrayList<>();
        }

        return comments.stream()
                .map(comment -> {
                    User commentAuthor = userRepo.findById(comment.getAuthorId())
                            .orElseThrow(() -> new ResourceNotFound("User with id '" + comment.getAuthorId() + "' was not found"));

                    return new CommentOutputDTO(comment.getContent(), commentAuthor.getUsername(), commentAuthor.getEmail(), comment.getCreated_at());
                })
                .collect(Collectors.toList());
    }

    private PostOutputDTO post(Post post){

        User user = userRepo.findById(post.getAuthorId())
                .orElseThrow(() -> new ResourceNotFound("This post is not attached to an author!"));

        List<Comment> comments = commentRepo.findByPostId(post.getId());

        List<CommentOutputDTO> commentDTOs = commentDTO(comments);

        return new PostOutputDTO(post.getTitle(), post.getContent(), user.getUsername(), user.getEmail(), post.getCreated_at(), post.getUpdated_at(), commentDTOs);
    }

    public PostOutputDTO createPost(PostInputDTO postInputDTO, HttpServletRequest request) {
        //Extract Email from claims
        String email = jwtService.getEmailFromRequest(request);

        // Find the user by email from PostgresSQL
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User with email '" + email + "' was not found"));

        // Create A Post
        Post post  = new Post();

        post.setTitle(postInputDTO.getTitle());
        post.setContent(postInputDTO.getContent());
        post.setAuthorId(user.getId());
        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());

        postRepo.save(post);

        return post(post);
    }

    public Page<PostOutputDTO> getAllPosts(int page, int size, String sortBy, String direction){
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postsPage = postRepo.findAll(pageable);

        return postsPage.map(this::post);
    }

    public PostOutputDTO getPostById(String postId) {
        return postRepo.findById(postId)
                .map(this::post)
                .orElseThrow(() -> new ResourceNotFound("Post with id '" + postId + "' was not found"));
    }

    public Page<PostOutputDTO> getPostsByAuthor(String authorUsername, int page, int size, String sortBy, String direction) {

        List<User> users = userRepo.findByUsername(authorUsername);

        if (users==null || users.isEmpty()){
            throw new ResourceNotFound("User with name '" + authorUsername + "' was not found");
        }

        List<Integer> userIds = users.stream()
                .map(User::getId)
                .toList();

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postsPage = postRepo.findByAuthorIdIn(userIds, pageable);

        return postsPage.map(this::post);
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

    @Transactional
    public void deletePost(String postId) {
        if(!postRepo.existsById(postId)){
            throw new ResourceNotFound("Post with id '" + postId + "' was not found");
        }

        commentService.deleteCommentByPostId(postId);

        postRepo.deleteById(postId);

    }

    @Transactional
    public void deletePostByAuthorId(int authorId) {
        List<Post> posts = postRepo.findByAuthorId(authorId);

        for (Post post : posts) {
            commentService.deleteCommentByPostId(post.getId());
        }
        postRepo.deleteAll(posts);
    }

}

