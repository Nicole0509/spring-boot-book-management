package org.example.blogmanagement.DTOs.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {
    private String username;
    private String password;
    private String email;
}
