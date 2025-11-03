package org.example.blogmanagement.Repositories;

import org.example.blogmanagement.Models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
    boolean existsById(String id);

    List<Post> findByAuthorId(int authorId);

    Page<Post> findByAuthorId(int authorId, Pageable pageable);
}
