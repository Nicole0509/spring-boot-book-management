package org.example.blogmanagement.Repositories;

import org.example.blogmanagement.Models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByPostId(String postId);
    boolean existsById(String id);
}
