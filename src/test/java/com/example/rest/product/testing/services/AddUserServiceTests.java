package com.example.rest.product.testing.services;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.exceptions.ExistingUserException;
import com.example.rest.product.testing.models.CreateUser;
import com.example.rest.product.testing.reposiories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AddUserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AddUserService addUserService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("AddUserService add user with non existing email")
    void addUserWithNonExistingEmail() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        var createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var savedUser = addUserService.execute(createUser);

        assertThat(savedUser).isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("AddUserService add user with existing email")
    void addUserWithExistingEmail() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        var createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        assertThrows(ExistingUserException.class, () -> addUserService.execute(createUser));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(0)).save(user);
    }
}
