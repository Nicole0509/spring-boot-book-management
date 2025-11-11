package org.example.blogmanagement.Services;

import jakarta.validation.Valid;
import org.example.blogmanagement.DTOs.Authentication.LoginDTO;
import org.example.blogmanagement.DTOs.Authentication.RegistrationDTO;
import org.example.blogmanagement.DTOs.User.UserInputDTO;
import org.example.blogmanagement.DTOs.User.UserResponseDTO;
import org.example.blogmanagement.Exceptions.ResourceAlreadyExists;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); //using 2^12 rounds of hashing

    public UserResponseDTO registerUser (RegistrationDTO registrationDTO) {
        //Check if an email is not already taken
        if(userRepo.existsByEmail(registrationDTO.getEmail())) {
            throw new ResourceAlreadyExists("User with email '" + registrationDTO.getEmail() + "' already exists!");
        }

        User user = new User();

        user.setEmail(registrationDTO.getEmail());
        user.setPassword(encoder.encode(registrationDTO.getPassword()));
        user.setUsername(registrationDTO.getUsername());

        userRepo.save(user);

        return new UserResponseDTO(user.getUsername(),user.getEmail());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(int id) {
        return userRepo.findById(id)
                .map(user -> new UserResponseDTO(user.getUsername(),user.getEmail()))
                .orElseThrow(() -> new ResourceNotFound("User with id '" + id + "' was not found!"));
    }

    public UserResponseDTO updateUserById(int id, UserInputDTO userInputDTO) {
        // Checking if a user exists
        if(!userRepo.existsById(id)) {
            throw new ResourceNotFound("User with id '" + id + "' was not found!");
        }

        //Check if an email is not already taken
        if(userRepo.existsByEmail(userInputDTO.getEmail())) {
            throw new ResourceAlreadyExists("User with email '" + userInputDTO.getEmail() + "' already exists!");
        }

        // Updating a user
        userRepo.findById(id).ifPresent(user -> {

            user.setEmail(userInputDTO.getEmail());
            user.setPassword(userInputDTO.getPassword());
            user.setUsername(userInputDTO.getUsername());

            userRepo.save(user);
        });

        return getUserById(id);
    }

    @Transactional
    public void deleteUserById(int id) {
        if(!userRepo.existsById(id)) {
            throw new ResourceNotFound("User with id '" + id + "' was not found!");
        }

        commentService.deleteCommentByAuthorId(id);
        postService.deletePostByAuthorId(id);
        userRepo.deleteById(id);
    }

    public String verify(@Valid LoginDTO loginDTO)  {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginDTO.getEmail());
        }

        return "fail";
    }
}
