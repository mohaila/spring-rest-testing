package com.example.rest.product.testing.reposiories;

import com.example.rest.product.testing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
