package org.example.blogmanagement.DTOs.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInputDTO {
    private String content;
    private String author_email;
}
