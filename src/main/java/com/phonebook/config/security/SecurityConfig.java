package com.phonebook.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService detailService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/user/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").successHandler(successHandler)
                .usernameParameter("username").passwordParameter("password")
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .clearAuthentication(true).invalidateHttpSession(true)
                .and().httpBasic();
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();

        successHandler.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws IOException {
                HttpSession session = httpServletRequest.getSession();
                SecurityUser authUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                session.setAttribute("username", authUser.getUsername());

                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                httpServletResponse.sendRedirect("/profile");
            }
        });
        return successHandler;
    }
    @Bean
    public UserDetailsService detailService() throws Exception {
        return new UserDetailsServiceImpl();
    }


}
