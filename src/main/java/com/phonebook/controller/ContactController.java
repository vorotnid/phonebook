package com.phonebook.controller;

import com.phonebook.entity.Contact;
import com.phonebook.entity.User;
import com.phonebook.exception.PhoneNumberDuplicateException;
import com.phonebook.service.ContactService;
import com.phonebook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/{username}/contacts")
public class ContactController {

    private final static Logger LOG = LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    public ContactController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    public ContactController() {
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String addContact(@Valid Contact contact, Errors errors, Model model, @PathVariable String username,
                             Principal principal, RedirectAttributes attributes) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't add records to other peoples phonebooks");
            return "redirect:/error";
        }
        if (errors.hasErrors()) {
            model.addAttribute("contactList", contactService.getAllContacts(username));
            model.addAttribute("user", userService.findUserByUsername(username));
            return "contacts";
        }
        try {
            Contact newContact = contactService.createNewContact(contact, username);
            return "redirect:/user/" + username + "/contacts/all";
        } catch (PhoneNumberDuplicateException e) {
            model.addAttribute("duplicate", true);
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("contactList", contactService.getAllContacts(username));
            model.addAttribute("user", userService.findUserByUsername(username));
            return "contacts";
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "firstName") String firstName,
                         @RequestParam(value = "lastName") String lastName,
                         @RequestParam(value = "cellPhone") String cellPhone,
                         @PathVariable String username, Model model, Principal principal,
                         RedirectAttributes attributes) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't view other people's records");
            return "redirect:/error";
        }
        List<Contact> contactList = contactService.findByRequestedParameters(firstName, lastName,
                cellPhone, username);
        model.addAttribute("user", userService.findUserByUsername(username));
        model.addAttribute("contact", new Contact());
        model.addAttribute("contactList", contactList);
        model.addAttribute("searchResult", true);
        return "contacts";
    }

    @RequestMapping(value = "/{contactId}", method = RequestMethod.GET)
    public String showContact(@PathVariable String contactId, Model model, @PathVariable String username,
                              Principal principal, RedirectAttributes attributes) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't view other people's records");
            return "redirect:/error";
        }
        Contact contact = contactService.getContactById(Long.valueOf(contactId), username);
        contact.setUser(userService.findUserByUsername(username));
        model.addAttribute("contact", contact);
        return "contact";
    }

    @RequestMapping(value = "/edit/{contactId}", method = RequestMethod.POST)
    public String editContact(@Valid Contact contact, Errors errors, @PathVariable String contactId,
                              Model model, @PathVariable String username, Principal principal, RedirectAttributes attributes) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't edit other people's records");
            return "redirect:/error";
        }
        if(contact.getUser() == null){
            contact.setUser(userService.findUserByUsername(username));
        }
        contact.setId(Long.valueOf(contactId));
        if (errors.hasErrors()) {
            model.addAttribute("contact", contact);
            return "contact";
        }

        contact = contactService.updateContact(contact);
        if (contact.getUser() == null)
            contact.setUser(userService.findUserByUsername(username));
        model.addAttribute("contact", contact);
        model.addAttribute("message", "Successfully edited!");
        return "contact";
    }

    @RequestMapping(value = "/delete/{contactId}", method = RequestMethod.GET)
    public String deleteContact(RedirectAttributes attributes, @PathVariable String contactId, @PathVariable String username,
                                Principal principal) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't delete other people's records");
            return "redirect:/error";
        }
        contactService.deleteContactById(Long.valueOf(contactId), username);
        return "redirect:/user/" + username + "/contacts/all";
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String showAllContacts(Model model, @PathVariable String username, Principal principal, RedirectAttributes attributes) {
        String pUsername = principal.getName();
        if(!username.equals(pUsername)){
            attributes.addFlashAttribute("errorMessage", "You can't view other people's records");
            return "redirect:/error";
        }
        if (!model.containsAttribute("user")) {
            User user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
        }
        List<Contact> contactList = contactService.getAllContacts(username);
        model.addAttribute("contact", new Contact());
        model.addAttribute("contactList", contactList);
        return "contacts";
    }
}
