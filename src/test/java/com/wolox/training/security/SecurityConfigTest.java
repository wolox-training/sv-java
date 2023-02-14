package com.wolox.training.security;

import com.wolox.training.controllers.UserController;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SecurityConfig.class)
class SecurityConfigTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mock;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    UserController userController;

    User user;

    UserDetails userDetails;

    @BeforeEach
    public void setup() {
        mock = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        List<Book> books = new ArrayList<Book>();
        User user = new User("user","user", LocalDate.of(2022,02,02), books,"ADMIN",
                "$2a$10$dpeJJWGeZBFgPwQLT4Sf4OQKK7lvOA0C4p3DN/H7jhb.mMH56GbGK");

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
        userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);

    }


    @DisplayName("whenAuthorized_returnsTheRequestAndStatusOk")
    @Test
    void configure() throws Exception {
        long id =1;
        when(userDetailsServiceImpl.loadUserByUsername("user")).thenReturn(userDetails);

        this.mock
                .perform(
                        get("/api/users/"+id)
                                .with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
                                .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @DisplayName("whenNotAuthorized_returnsIsUnauthorized")
    @Test
    void configureInvalid() throws Exception {
        long id =1;
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDetailsEmpty = new org.springframework.security.core.userdetails.User("user2",
                Strings.EMPTY, authorities);

        when(userDetailsServiceImpl.loadUserByUsername(any())).thenReturn(userDetailsEmpty);
        this.mock
                .perform(
                        get("/api/users/"+id)
                                .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

}