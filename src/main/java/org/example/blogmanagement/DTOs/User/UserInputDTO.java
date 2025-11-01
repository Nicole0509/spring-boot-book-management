package org.example.blogmanagement.DTOs.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {
    @Valid

    @NotNull(message = "username is mandatory")
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotNull(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Size(min = 5, max = 20, message = "password must be between 5 and 20 characters.")
    private String password;

    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "Insert a valid email")
    private String email;
}
