package org.example.blogmanagement.Services;

import org.example.blogmanagement.DTOs.UserInputDTO;
import org.example.blogmanagement.DTOs.UserResponseDTO;
import org.example.blogmanagement.Models.User;
import org.example.blogmanagement.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

}
