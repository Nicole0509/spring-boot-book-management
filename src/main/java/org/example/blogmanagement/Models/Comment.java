package org.example.blogmanagement.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field(name = "content")
    private String content;

    @Field(name = "author_id")
    private int authorId;

    @Field(name = "postId")
    private String postId;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime created_at;

    @LastModifiedDate
    @Field(name = "updated_at")
    private LocalDateTime  updated_at;

}
