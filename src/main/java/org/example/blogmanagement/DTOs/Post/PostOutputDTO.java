package org.example.blogmanagement.DTOs.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blogmanagement.DTOs.Comment.CommentOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOutputDTO {
    private String title;
    private String content;
    private String author;
    private String author_email;
    private LocalDateTime created_at;
    private LocalDateTime  updated_at;
    private List<CommentOutputDTO> comments;
}
