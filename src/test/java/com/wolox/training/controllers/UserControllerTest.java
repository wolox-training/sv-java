package com.wolox.training.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Book book;

    @BeforeEach
    void setUp(){
        book =  new Book("Terror", "Stephen King", "abcd", "IT", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");

        List<Book> books = new ArrayList<Book>();
        user = new User("user1", "user1",  LocalDate.of(2022,02,02), books);
    }

    @DisplayName("User load in the db correct parameters")
    @Test
    void create() throws Exception{
       when(userRepository.save(user)).thenReturn(user);
       mvc.perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isCreated())
               .andDo(print());
    }

    @DisplayName("User: Successfully delete user")
    @Test
    void delete() throws Exception{
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("User: Exception when delete user")
    @Test
    void deleteInvalid() throws Exception {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("User: Successfully update")
    @Test
    void updateUser() throws Exception{
        long id = 1;
        User user2 = new User("user2", "user2", LocalDate.of(2020,02,02), new ArrayList<Book>());

        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(userRepository.save(any(User.class))).thenReturn(user2);
        mvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user2.getName()))
                .andDo(print());

    }


    @DisplayName("User: List of users found successfully")
    @Test
    void findAll() throws Exception{
        User user2 = new User("user2", "user2", LocalDate.of(2020,02,02), new ArrayList<Book>());
        List<User> users = new ArrayList<>(Arrays.asList(user, user2));

        when(userRepository.findAll()).thenReturn(users);
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();

        mvc.perform(MockMvcRequestBuilders.get("/api/users").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()))
                .andDo(print());
    }

    @DisplayName("User: User found successfully")
    @Test
    void findOne() throws Exception{
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));


        mvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andDo(print());
    }

    @DisplayName("User: Book added successfully")
    @Test
    void addBook() throws Exception{
        long id = 1;
        book.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(userRepository.save(user)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/api/users/addbook/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].title").value(book.getTitle()))
                .andDo(print());
    }


    @DisplayName("User: Book removed successfully")
    @Test
    void removeBook()  throws Exception{
        long id = 1;
        book.setId(id);
        user.addBook(book);
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(userRepository.save(user)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/api/users/removebook/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books.size()").value(user.getBooks().size()))
                .andDo(print());
    }
}