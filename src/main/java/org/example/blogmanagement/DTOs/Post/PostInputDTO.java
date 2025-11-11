package org.example.blogmanagement.DTOs.Post;

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
public class PostInputDTO {
    @Valid

    @NotNull(message = "title is mandatory")
    @NotBlank(message = "title is mandatory")
    private String title;

    @NotNull(message = "content is mandatory")
    @NotBlank(message = "content is mandatory")
    @Size(min = 5, message = "content must be at least characters.")
    private String content;

}
