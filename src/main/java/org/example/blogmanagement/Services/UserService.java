package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.User.UserInputDTO;
import org.example.blogmanagement.DTOs.User.UserResponseDTO;
import org.example.blogmanagement.Exceptions.ResourceNotFound;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

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

    public UserResponseDTO getUserById(int id) {
        return userRepo.findById(id)
                .map(user -> new UserResponseDTO(user.getUsername(),user.getEmail()))
                .orElseThrow(() -> new ResourceNotFound("User with id '" + id + "' was not found!"));
    }

}
