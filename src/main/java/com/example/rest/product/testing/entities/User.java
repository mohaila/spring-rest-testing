package com.example.rest.product.testing.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {
    @jakarta.persistence.Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    private final String firstName;
    @Column(name = "last_name", nullable = false)
    private final String lastName;
    @Column(name = "email", nullable = false)
    private final String email;
}
