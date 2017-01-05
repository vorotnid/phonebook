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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

    @Test
    public void invalidUsernameValidation() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/register").param("username", "1aaaaa").param("firstName", "first")
                .param("lastName", "last").param("middleName", "middle").param("password", "password")).andDo(print())
                .andExpect(view().name("registerPage")).andExpect(model().hasErrors());
    }
    
    @Test
    public void UsernameValidation() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/register").param("username", "1aaaaa").param("firstName", "first")
                .param("lastName", "last").param("middleName", "middle").param("password", "password")).andDo(print())
                .andExpect(view().name("registerPage")).andExpect(model());
    }

    @Test
    public void shouldProcessRegistration() throws Exception {
        UserService service = mock(UserService.class);
        when(service.register(unsaved)).thenReturn(saved);

        UserController controller = new UserController(service);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(post("/user/register")
                .param("username", unsaved.getUsername()).param("firstName", unsaved.getFirstName())
                .param("lastName", unsaved.getLastName()).param("middleName", unsaved.getMiddleName())
                .param("password", unsaved.getPassword()))
                .andExpect(redirectedUrl("/user/" + saved.getUsername() + "/contacts/all"));
        verify(service, atLeastOnce()).register(unsaved);
    }

    @Test
    public void shouldShowregisterPage() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/user/register")).andExpect(view().name("registerPage"));
    }
}
