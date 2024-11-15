package com.example.rest.product.testing.controllers;

import com.example.rest.product.testing.entities.User;
import com.example.rest.product.testing.exceptions.ExistingUserException;
import com.example.rest.product.testing.exceptions.Messages;
import com.example.rest.product.testing.models.CreateUser;
import com.example.rest.product.testing.services.AddUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisabledInAotMode
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddUserService addUserService;

    @Test
    @DisplayName("UserController create user with new email")
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
        given(addUserService.execute(any(CreateUser.class))).willReturn(user);

        var response = mockMvc.perform(post(Endpoints.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is((int) user.getId())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("UserController create user with existing email")
    public void createUserWithExistingEmail() throws Exception {
        var createUser = CreateUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@does.org")
                .build();
        given(addUserService.execute(any(CreateUser.class))).willThrow(ExistingUserException.class);

        var response = mockMvc.perform(post(Endpoints.USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUser)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(Messages.EMAIL_ERROR)))
                .andDo(print());
    }
}
