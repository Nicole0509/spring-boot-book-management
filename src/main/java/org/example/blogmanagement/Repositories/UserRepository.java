package org.example.blogmanagement.Repositories;

import org.example.blogmanagement.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
