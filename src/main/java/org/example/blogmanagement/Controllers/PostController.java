package org.example.blogmanagement.Controllers;


import org.example.blogmanagement.DTOs.Post.PostInputDTO;
import org.example.blogmanagement.DTOs.Post.PostOutputDTO;
import org.example.blogmanagement.Services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostOutputDTO createPost(@RequestBody PostInputDTO postInputDTO){
        return postService.createPost(postInputDTO);
    }

    @GetMapping
    public List<PostOutputDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostOutputDTO getPost(@PathVariable String id){
        return postService.getPostById(id);
    }

    @PatchMapping("/{id}")
    public PostOutputDTO updatePost(@PathVariable String id, @RequestBody PostInputDTO postInputDTO){
        return postService.updatePost(id, postInputDTO);
    }
}
