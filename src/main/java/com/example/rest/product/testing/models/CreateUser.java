package com.example.rest.product.testing.models;

import com.example.rest.product.testing.entities.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUser {
    private final String firstName;
    private final String lastName;
    private final String email;

    public User toEntity() {
        return User.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .build();
    }
}
