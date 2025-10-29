package org.example.blogmanagement.DTOs.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutputDTO {
    private String content;
    private Long author_id;
    private LocalDateTime updated_at;
}
