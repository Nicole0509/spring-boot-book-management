package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.User.UserInputDTO;
import org.example.blogmanagement.DTOs.User.UserResponseDTO;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserResponseDTO createUser (UserInputDTO userInputDTO) {
        User user = new User();

        user.setEmail(userInputDTO.getEmail());
        user.setPassword(userInputDTO.getPassword());
        user.setUsername(userInputDTO.getUsername());

        userRepo.save(user);

        return new UserResponseDTO(user.getUsername(),user.getEmail());
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

}
