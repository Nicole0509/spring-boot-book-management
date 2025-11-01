package org.example.blogmanagement.Controllers;

import org.example.blogmanagement.DTOs.Comment.CommentInputDTO;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentOutputDTO createComment(@RequestBody CommentInputDTO commentInputDTO) {
        return commentService.createComment(commentInputDTO);
    }

    @GetMapping("/{id}")
    public CommentOutputDTO getComment(@PathVariable String id) {
        return commentService.getAllCommentById(id);
    }

}
