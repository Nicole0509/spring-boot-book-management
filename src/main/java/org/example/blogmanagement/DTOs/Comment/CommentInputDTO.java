package org.example.blogmanagement.DTOs.Comment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInputDTO {
    @NotNull(message = "content is mandatory")
    @NotBlank(message = "content is mandatory")
    private String content;

    @NotNull(message = "post_id is mandatory"  )
    @NotBlank(message = "post_id is mandatory")
    private String post_id;
}
