package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Comment.CommentInputDTO;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.Comment;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.CommentRepository;
import org.example.blogmanagement.Repositories.PostRepository;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    public CommentOutputDTO createComment(CommentInputDTO commentInputDTO) {
        // Find the user by email from PostgreSQL
        User user = userRepo.findByEmail(commentInputDTO.getAuthor_email())
                .orElseThrow(() -> new ResourceNotFound("User with email '" + commentInputDTO.getAuthor_email() + "' was not found"));

        // Check if a post exists
        Post post = postRepo.findById(commentInputDTO.getPost_id())
                .orElseThrow(() -> new  ResourceNotFound("Post with id '" + commentInputDTO.getPost_id() + "' was not found"));

        //Writing a comment
        Comment comment = new Comment();

        comment.setContent(commentInputDTO.getContent());
        comment.setAuthor_id(user.getId());
        comment.setPost_id(post.getId());

        commentRepo.save(comment);

        return new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at());
    }
}
