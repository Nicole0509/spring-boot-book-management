package org.example.blogmanagement.Controllers;

import org.example.blogmanagement.DTOs.UserInputDTO;
import org.example.blogmanagement.DTOs.UserResponseDTO;
import org.example.blogmanagement.Services.UserService;
import org.springframework.web.bind.annotation.*;

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
}
