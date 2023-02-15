package com.wolox.training.controllers;

import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mock;

    private User user;

    @BeforeEach
    public void setup() {

        List<Book> books = new ArrayList<Book>();
        user = new User("user", "user", LocalDate.of(2022, 02, 02), books, "ADMIN",
                "1234");
    }


    @WithMockUser(value = "user")
    @Test
    void loggedUser()throws Exception{

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userRepository.findByUsername(any())).thenReturn(Optional. of(user));

        this.mock.perform(MockMvcRequestBuilders.get("/api/me"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}