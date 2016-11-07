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

import java.util.ArrayList;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
        contact = new Contact("first", "last", "middle", "+380(66)6666666", "", "", "");
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

    @Test
    public void shouldProcessAddTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/user/username/contacts/new").param("firstName", contact.getFirstName())
                .param("lastName", contact.getLastName()).param("middleName", contact.getMiddleName())
                .param("cellPhoneNumber", contact.getCellPhoneNumber()).param("homePhoneNumber", contact.getHomePhoneNumber())
                .param("email", contact.getEmail()).param("address", contact.getAddress())
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print()).andExpect(redirectedUrl("/user/username/contacts/all"));

        verify(contactService, atLeastOnce()).createNewContact(contact, user.getUsername());
    }

    @Test
    public void searchTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(contactService.findByRequestedParameters(contact.getFirstName(), contact.getLastName(), contact.getCellPhoneNumber(), user.getUsername())).thenReturn(new ArrayList<Contact>());
        mockMvc.perform(get("/user/username/contacts/search").param("firstName", contact.getFirstName())
                .param("lastName", contact.getLastName())
                .param("cellPhone", contact.getCellPhoneNumber())
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print()).andExpect(view().name("contacts")).andExpect(model().attributeExists("searchResult"));
        verify(contactService, atLeastOnce()).findByRequestedParameters(contact.getFirstName(), contact.getLastName(), contact.getCellPhoneNumber(), user.getUsername());
        verify(userService, atLeastOnce()).findUserByUsername(user.getUsername());
    }

    @Test
    public void showAllContactsTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/user/username/contacts/all")
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print())
                .andExpect(view().name("contacts"))
                .andExpect(model().attributeExists("contactList"));
        verify(userService, atLeastOnce()).findUserByUsername(user.getUsername());
        verify(contactService, atLeastOnce()).getAllContacts(user.getUsername());
    }

    @Test
    public void deleteContactTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/user/username/contacts/delete/" + contact.getId())
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print())
                .andExpect(redirectedUrl("/user/username/contacts/all"));

        verify(contactService, atLeastOnce()).deleteContactById(contact.getId(), user.getUsername());
    }

    @Test
    public void shouldShowContact() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(contactService.getContactById(contact.getId(), user.getUsername())).thenReturn(contact);
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        mockMvc.perform(get("/user/username/contacts/" + contact.getId())
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contact"));
        verify(contactService, atLeastOnce()).getContactById(contact.getId(), user.getUsername());
    }

    @Test
    public void failEditContactValidationTest() throws Exception {
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(contactService.getContactById(contact.getId(), user.getUsername())).thenReturn(contact);
        contact.setId(1);
        mockMvc.perform(post("/user/username/contacts/edit/" + contact.getId())
                .param("firstName", "a").param("lastName", contact.getLastName()).param("middleName", contact.getMiddleName())
                .param("cellPhoneNumber", "38090959642")
                .principal(SecurityContextHolder.getContext().getAuthentication())).andDo(print())
                .andExpect(view().name("contact")).andExpect(model().attributeHasFieldErrors("contact", "firstName", "cellPhoneNumber"));

    }

    @Test
    public void editContactSuccessTest() throws Exception {
        contact.setId(1);
        Contact updated = contact;
        updated.setFirstName("edited");
        updated.setCellPhoneNumber("+380(95)0959642");
        MockMvc mockMvc = standaloneSetup(controller).build();
        when(contactService.updateContact(contact)).thenReturn(updated);
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        mockMvc.perform(post("/user/username/contacts/edit/" + contact.getId())
                .param("firstName", updated.getFirstName())
                .param("lastName", contact.getLastName())
                .param("middleName", contact.getMiddleName())
                .param("cellPhoneNumber", updated.getCellPhoneNumber())
                .param("email", "").param("address", "")
                .param("homePhoneNumber", "")
                .principal(SecurityContextHolder.getContext().getAuthentication()))
                .andDo(print())
                .andExpect(view().name("contact")).andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attribute("contact", updated));
        verify(contactService, atLeastOnce()).updateContact(contact);
        verify(userService, atLeastOnce()).findUserByUsername(user.getUsername());
    }
}
