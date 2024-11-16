package com.example.rest.product.testing.controllers;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.exceptions.Messages;
import com.example.rest.product.testing.models.CreateUser;
import com.example.rest.product.testing.reposiories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("UserController integration test to create user with new email")
    public void createUserWithNewEmail() throws Exception {
        var createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        var response = mockMvc.perform(post(Endpoints.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("UserController integration test to create user with existing email")
    public void createUserWithExistingEmail() throws Exception {
        var createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();

        var response = mockMvc.perform(post(Endpoints.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser)));

        response = mockMvc.perform(post(Endpoints.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(Messages.EMAIL_ERROR)))
                .andDo(print());
    }
}
