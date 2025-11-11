package org.example.blogmanagement.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.blogmanagement.DTOs.Comment.CommentInputDTO;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.Comment;
import org.example.blogmanagement.Models.Post;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.CommentRepository;
import org.example.blogmanagement.Repositories.PostRepository;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private JWTService jwtService;

    private CommentOutputDTO comment (Comment comment, User user){
        return new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at());
    }

    public CommentOutputDTO createComment(CommentInputDTO commentInputDTO, HttpServletRequest request) {
        //Extract Email from claims
        String email = jwtService.getEmailFromRequest(request);

        // Find the user by email from PostgresSQL
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User with email '" + email + "' was not found"));

        // Check if a post exists
        Post post = postRepo.findById(commentInputDTO.getPost_id())
                .orElseThrow(() -> new  ResourceNotFound("Post with id '" + commentInputDTO.getPost_id() + "' was not found"));

        //Writing a comment
        Comment comment = new Comment();

        comment.setContent(commentInputDTO.getContent());
        comment.setAuthorId(user.getId());
        comment.setPostId(post.getId());

        commentRepo.save(comment);

        return comment(comment, user);
    }

    public CommentOutputDTO getAllCommentById(String id) {
        return commentRepo.findById(id)
                .map(comment -> {
                    User user = userRepo.findById(comment.getAuthorId())
                            .orElseThrow(() -> new ResourceNotFound("This comment is not attached to an author"));

                    return comment(comment, user);
                })
                .orElseThrow(() -> new ResourceNotFound("Comment with ID '" + id + "' was not found"));
    }

    public CommentOutputDTO updateComment(CommentInputDTO commentInputDTO, String commentId, HttpServletRequest request) {

        //Get the logged-in email
        String email = jwtService.getEmailFromRequest(request);

        //Find user email in the DB
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User with email '" + email + "' was not found"));

        //Load the post from DB
        Post post = postRepo.findById(commentInputDTO.getPost_id())
                .orElseThrow(() -> new ResourceNotFound("Post with id '" + commentInputDTO.getPost_id() + "' not found"));

        //Load the comment from DB
        Comment comment  = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment with id '" + commentId + "' not found"));

        //Check if the comment belongs to the user
        if(comment.getAuthorId() != user.getId()){
            throw new AccessDeniedException("You cannot edit someone else's comment");
        }

        // Patching a comment's content
        comment.setContent(commentInputDTO.getContent());
        comment.setUpdated_at(LocalDateTime.now());
        commentRepo.save(comment);

        return comment(comment, user);
    }

    public void deleteComment(String commentId, HttpServletRequest request) {
        //Get the logged-in email
        String email = jwtService.getEmailFromRequest(request);

        //Find user email in the DB
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User with email '" + email + "' was not found"));

        //Load the comment from DB

        if (!commentRepo.existsById(commentId)) {
            throw new ResourceNotFound("Comment with ID '" + commentId + "' was not found");
        }

        Comment comment  = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment with id '" + commentId + "' not found"));

        //Check if the comment belongs to the user
        if(comment.getAuthorId() != user.getId()){
            throw new AccessDeniedException("You cannot edit someone else's comment");
        }

        commentRepo.deleteById(commentId);
    }

    public void deleteCommentByPostId(String postId) {
        commentRepo.deleteByPostId(postId);
    }

    public void deleteCommentByAuthorId(int authorId) {
        commentRepo.deleteByAuthorId(authorId);
    }
}
