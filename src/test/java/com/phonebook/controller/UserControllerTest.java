package com.phonebook.controller;

import com.phonebook.entity.User;
import com.phonebook.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest {

    static User unsaved;
    static User saved;
    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeClass
    public static void userInit() {
        unsaved = new User("username", "password", "first", "last", "middle");
        saved = new User(1L, "username", "password", "first", "last", "middle");
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFailValidationWithNoData() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/register")).andExpect(view().name("registerPage")).andExpect(model().hasErrors());
    }
}
