package org.example.blogmanagement.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.blogmanagement.DTOs.Comment.CommentInputDTO;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;
import org.example.blogmanagement.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Tag(name = "Comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            description = "This end point creates a new comment and assigns it to an author id and a post id to create a relationship.",
            summary = "Create a new comment",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409"
                    ),
            }
    )
    @PostMapping
    public CommentOutputDTO createComment(@Valid @RequestBody CommentInputDTO commentInputDTO) {
        return commentService.createComment(commentInputDTO);
    }

    @Operation(
            description = "This endpoint shows a single comment with its corresponding author name and email`",
            summary = "Get A Comment By Id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409"
                    ),
            }
    )
    @GetMapping("/{id}")
    public CommentOutputDTO getComment(@PathVariable String id) {
        return commentService.getAllCommentById(id);
    }

    @Operation(
            description = "This endpoint patches a comment's and leaves the rest as is",
            summary = "Patches A Comment's Content",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Conflict",
                            responseCode = "409"
                    ),
            }
    )
    @PatchMapping("/{id}")
    public CommentOutputDTO updateComment(@PathVariable String id, @Valid @RequestBody CommentInputDTO commentInputDTO) {
        return commentService.updateComment(commentInputDTO, id);
    }

    @Operation(
            description = "This endpoint deletes a comment and its associated details.",
            summary = "Deletes A Comment",
            responses = {
                    @ApiResponse(
                            description = "Success/No Content",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    ),
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }
}
