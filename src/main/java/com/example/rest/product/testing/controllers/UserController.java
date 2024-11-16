package com.example.rest.product.testing.controllers;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.models.CreateUser;
import com.example.rest.product.testing.services.AddUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.USERS)
public class UserController {
    private final AddUserService addUserService;

    public UserController(AddUserService addUserService) {
        this.addUserService = addUserService;
    }

    @PostMapping(produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody CreateUser createUser) {
        return addUserService.execute(createUser);
    }
}
