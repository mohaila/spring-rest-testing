package com.example.rest.product.testing.repositories;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.reposiories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("UserRepository save user")
    public void saveUser() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("UserRepository findAll when no users")
    public void findAllUsersNoUsers() {
        var users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("UserRepository findAll when users")
    public void findAllUsers() {
        var user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();

        var user2 = User.builder()
                .firstName("Mary")
                .lastName("Doe")
                .email("mary.doe@does.org")
                .build();
        var savedUser1 = userRepository.save(user1);
        var savedUser2 = userRepository.save(user2);

        var users = userRepository.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.getFirst()).isEqualTo(savedUser1);
        assertThat(users.getLast()).isEqualTo(savedUser2);
    }

    @Test
    @DisplayName("UserRepository find existing user by id")
    void findExistingUserById() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var savedUser = userRepository.save(user);

        var result = userRepository.findById(savedUser.getId());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(savedUser.getId());
        assertThat(result.get().getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(result.get().getLastName()).isEqualTo(savedUser.getLastName());
        assertThat(result.get().getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    @DisplayName("UserRepository find non existing user by id")
    void findNonExistingUserById() {
        var result = userRepository.findById(1L);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("UserRepository delete existing user")
    void deleteExistingUser() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var savedUser = userRepository.save(user);
        var id = savedUser.getId();
        userRepository.deleteById(id);

        var result = userRepository.findById(id);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("UserRepository update existing user")
    void updateExistingUser() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var savedUser = userRepository.save(user);
        user = User.builder()
                .id(savedUser.getId())
                .firstName("Mary")
                .lastName("Doe")
                .email("mary.doe@does.org")
                .build();
        var updatedUser = userRepository.save(user);
    }
}
