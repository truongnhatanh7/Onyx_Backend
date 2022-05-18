package com.rmit.onyx2.controller;

import com.rmit.onyx2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private UserService userService;

    @Test
    void getAllUsers() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user/all-users/");
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    void getUserById() {
    }

    @Test
    void addUser() {
    }

    @Test
    void editUser() {
    }

    @Test
    void addWorkspaceForUserById() {
    }

    @Test
    void removeUserFromWorkspaceById() {
    }

    @Test
    void deleteUserById() {
    }
}