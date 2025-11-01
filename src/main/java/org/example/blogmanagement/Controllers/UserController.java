package org.example.blogmanagement.Controllers;

import org.example.blogmanagement.DTOs.User.UserInputDTO;
import org.example.blogmanagement.DTOs.User.UserResponseDTO;
import org.example.blogmanagement.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UserResponseDTO createUser (@RequestBody UserInputDTO userInputDTO){
        return userService.createUser(userInputDTO);
    }

    @GetMapping()
    public List<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }
}
