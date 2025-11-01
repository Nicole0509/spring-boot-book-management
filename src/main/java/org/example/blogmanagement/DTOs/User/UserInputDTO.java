package org.example.blogmanagement.DTOs.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {
    @NotNull(message = "username is mandatory")
    @NotBlank(message = "username is mandatory")
    private String username;
    private String password;
    private String email;
}
