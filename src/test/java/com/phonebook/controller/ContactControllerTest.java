package com.phonebook.controller;

import com.phonebook.entity.Contact;
import com.phonebook.entity.User;
import com.phonebook.service.ContactService;
import com.phonebook.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContactController controller;

    private static Contact contact;

    private static User user;

    private MockMvc mockMvc;

    @Before
    public void initMocks() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void domainObjectsInit() {
        user = new User(1L, "username", "password", "first", "last", "middle");
        contact = null; //new Contact("first", "last", "middle", "+380(66)6666666", "", "", "");
    }

    @Test
    public void shouldFailValidationCellNumber() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/username/contacts/new").param("firstName", contact.getFirstName())
                .param("lastName", contact.getLastName()).param("middleName", contact.getMiddleName())
                .param("cellPhoneNumber", "+380666666666")
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print())
                .andExpect(view().name("contacts"))
                .andExpect(model().attributeHasFieldErrors("contact", "cellPhoneNumber"));
    }

    @Test
    public void shouldFailValidationEmailTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/username/contacts/new").param("firstName", contact.getFirstName())
                .param("lastName", contact.getLastName()).param("middleName", contact.getMiddleName())
                .param("cellPhoneNumber", contact.getCellPhoneNumber()).param("email", "email")
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print()).andExpect(view().name("contacts"))
                .andExpect(model().attributeHasFieldErrors("contact", "email"));
    }

    @Test
    public void shouldFailValidationTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/username/contacts/new").param("firstName", "")
                .param("lastName", "").param("middleName", "")
                .param("cellPhoneNumber", "").param("email", "")
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print()).andExpect(view().name("contacts"))
                .andExpect(model().attributeHasFieldErrors("contact", "firstName", "lastName", "middleName", "cellPhoneNumber"));
    }
}
