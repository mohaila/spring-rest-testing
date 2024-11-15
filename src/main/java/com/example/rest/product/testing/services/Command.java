package com.example.rest.product.testing.services;

public interface Command<I, O> {
    O execute(I input);
}
