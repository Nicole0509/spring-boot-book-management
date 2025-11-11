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

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    public CommentOutputDTO createComment(CommentInputDTO commentInputDTO) {
        // Find the user by email from PostgresSQL
        User user = userRepo.findByEmail(commentInputDTO.getAuthor_email())
                .orElseThrow(() -> new ResourceNotFound("User with email '" + commentInputDTO.getAuthor_email() + "' was not found"));

        // Check if a post exists
        Post post = postRepo.findById(commentInputDTO.getPost_id())
                .orElseThrow(() -> new  ResourceNotFound("Post with id '" + commentInputDTO.getPost_id() + "' was not found"));

        //Writing a comment
        Comment comment = new Comment();

        comment.setContent(commentInputDTO.getContent());
        comment.setAuthorId(user.getId());
        comment.setPostId(post.getId());

        commentRepo.save(comment);

        return new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at());
    }

    public CommentOutputDTO getAllCommentById(String id) {
        return commentRepo.findById(id)
                .map(comment -> {
                    User user = userRepo.findById(comment.getAuthorId())
                            .orElseThrow(() -> new ResourceNotFound("This comment is not attached to an author"));

                    return new CommentOutputDTO(comment.getContent(), user.getUsername(), user.getEmail(), comment.getCreated_at());
                })
                .orElseThrow(() -> new ResourceNotFound("Comment with ID '" + id + "' was not found"));
    }


    public CommentOutputDTO updateComment(CommentInputDTO commentInputDTO, String commentId) {

        // Patching a comment's content
        return commentRepo.findById(commentId).map(
                comment -> {
                    comment.setContent(commentInputDTO.getContent());
                    comment.setUpdated_at(LocalDateTime.now());
                    commentRepo.save(comment);

                    return getAllCommentById(commentId);
                }
        ).orElseThrow(() -> new ResourceNotFound("Comment with ID '" + commentId + "' was not found"));
    }

    public void deleteComment(String commentId) {
        if (!commentRepo.existsById(commentId)) {
            throw new ResourceNotFound("Comment with ID '" + commentId + "' was not found");
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
