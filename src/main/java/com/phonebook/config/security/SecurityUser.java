package com.phonebook.config.security;

import com.phonebook.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser extends User implements UserDetails {

    SecurityUser(User user) {
        if (user != null) {
            this.setId(user.getId());
            this.setFirstName(user.getFirstName());
            this.setLastName(user.getLastName());
            this.setMiddleName(user.getMiddleName());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
