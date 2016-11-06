package com.phonebook.controller;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class MainControllerTest {

    @Test
    public void shouldShowLoginPage() throws Exception {
        MainController controller = new MainController();
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/login")).andExpect(view().name("loginPage"));
    }
}
