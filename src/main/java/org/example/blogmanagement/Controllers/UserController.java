package org.example.blogmanagement.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.blogmanagement.DTOs.User.UserInputDTO;
import org.example.blogmanagement.DTOs.User.UserResponseDTO;
import org.example.blogmanagement.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "This end point creates a new user whose email must be unique otherwise an error is thrown.",
            summary = "Create a new user",
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
    @PostMapping("/register")
    public UserResponseDTO registerUser (@Valid @RequestBody UserInputDTO userInputDTO){
        return userService.registerUser(userInputDTO);
    }

    @Operation(
            description = "This endpoint generates a list of all users in the DB and their emails",
            summary = "Get All Users in the DB",
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
    @GetMapping("/user")
    public List<UserResponseDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @Operation(
            description = "This endpoint shows a single users and the corresponding email`",
            summary = "Get A User By Id",
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
    @GetMapping("/user/{id}")
    public UserResponseDTO getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @Operation(
            description = "This endpoint updates a user's credentials",
            summary = "Updates A User's Credentials",
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
    @PutMapping("/user/{id}")
    public UserResponseDTO updateUserById(@PathVariable int id,@Valid @RequestBody UserInputDTO userInputDTO){
        return userService.updateUserById(id, userInputDTO);
    }

    @Operation(
            description = "This endpoint deletes a user's credentials, all their corresponding posts and comments and all the comments related to this particular user's posts.",
            summary = "Deletes A User's Credentials and Cascades",
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
    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable int id){
        userService.deleteUserById(id);
    }
}
