package org.example.blogmanagement.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/post")
@Tag(name = "Post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            description = "This end point creates a new post in the DB.",
            summary = "Create a new post",
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
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutputDTO createPost(@Valid @RequestBody PostInputDTO postInputDTO, HttpServletRequest request){
        return postService.createPost(postInputDTO, request);
    }

    @Operation(
            description = "This endpoint displays all posts and each one has a list comments if it has any corresponding with its id. This method also renders everything in form of pages and applies some sorting as per the user requirements.",
            summary = "Get All posts with their associated comments",
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
            }
    )
    @GetMapping
    public Page<PostOutputDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String direction){

        return postService.getAllPosts(page, size, sortBy, direction);
    }

    @Operation(
            description = "This endpoint shows all posts by the same user.",
            summary = "Get All Post By Username",
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
    @GetMapping("/author/{username}")
    public Page<PostOutputDTO> getPostsByAuthor(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created_at") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return postService.getPostsByAuthor(username, page, size, sortBy, direction);
    }

    @Operation(
            description = "This endpoint shows a single post and the corresponding corresponding comments.",
            summary = "Get A Post By Id",
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
    public PostOutputDTO getPost(@PathVariable String id){
        return postService.getPostById(id);
    }

    @Operation(
            description = "This endpoint allows to patch a a post's title and content as well as the updated date. The rest of the data is conserved as is in the DB.",
            summary = "Patches A Post",
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
    public PostOutputDTO updatePost(@PathVariable String id, @Valid @RequestBody PostInputDTO postInputDTO, HttpServletRequest request){
        return postService.updatePost(id, postInputDTO, request);
    }

    @Operation(
            description = "This endpoint deletes a post its corresponding comments.",
            summary = "Deletes A Post's Credentials and Cascades",
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
    public void deletePost(@PathVariable String id, HttpServletRequest request){
        postService.deletePost(id, request);
    }

}
