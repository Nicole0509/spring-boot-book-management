package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.Comment.CommentInputDTO;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.Models.Comment;
import org.example.blogmanagement.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    public CommentOutputDTO createComment(CommentInputDTO commentInputDTO) {
        Comment comment = new Comment();

        comment.setContent(commentInputDTO.getContent());
        comment.setAuthor_id(1L);

        commentRepo.save(comment);

        return new CommentOutputDTO(comment.getContent(), comment.getAuthor_id(),comment.getCreated_at());
    }
}
