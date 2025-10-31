package org.example.blogmanagement.DTOs.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutputDTO {
    private String content;
    private String author;
    private String author_email;
    private LocalDateTime created_at;
}
