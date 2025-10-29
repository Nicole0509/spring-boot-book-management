package org.example.blogmanagement.Repositories;

import org.example.blogmanagement.Models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
