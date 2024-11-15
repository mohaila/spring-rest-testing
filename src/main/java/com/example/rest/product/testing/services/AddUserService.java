package com.example.rest.product.testing.services;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.exceptions.ExistingUserException;
import com.example.rest.product.testing.models.CreateUser;
import com.example.rest.product.testing.reposiories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AddUserService implements Command<CreateUser, User> {
    private final UserRepository userRepository;

    public AddUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute (CreateUser createUser) {
        var existingUser = userRepository.findByEmail(createUser.getEmail());
        if (existingUser.isPresent()) {
            throw new ExistingUserException();
        }
        var savedUser = userRepository.save(createUser.toEntity());
        return savedUser;
    }
}
