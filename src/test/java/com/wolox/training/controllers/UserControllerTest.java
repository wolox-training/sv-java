package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.exceptions.UserUsernameRepeatedException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mvc;

    private User user;

    private Book book;

    @BeforeEach
    void setUp(){
        book =  new Book("Terror", "Stephen King", "abcd", "IT", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
        book.setId(1);

        List<Book> books = new ArrayList<Book>();
        user = new User("user1", "user1", LocalDate.of(2022,02,02), books);
        user.setId(1);
    }

    @DisplayName("User: Successfully created")
    @Test
    void create() throws Exception{
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<Object> userReturn =userController.create(user);
        Assertions.assertEquals("user1",  ((User)userReturn.getBody()).getName());
        Assertions.assertEquals(HttpStatus.CREATED, userReturn.getStatusCode());
    }

    @DisplayName("User: Failed to create")
    @Test
    void createInvalid() {
        Optional<User>  userMock = Optional. of(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(userMock);
        Assertions.assertThrows(UserUsernameRepeatedException.class, ()-> userController.create(user), "the username is already registered");
    }

    @DisplayName("User: Successfully delete user")
    @Test
    void delete() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        ResponseEntity<String> userReturn = userController.delete(id);
        Assertions.assertEquals(HttpStatus.OK, userReturn.getStatusCode());
        Assertions.assertEquals("User has been successfully removed", ((String)userReturn.getBody()));
    }

    @DisplayName("User: Exception when delete user")
    @Test
    void deleteInvalid() {
        long id = 1;
        when(userRepository.findById(id)).thenThrow(new UserNotFoundException("User not found"));
        try {
            userController.delete(id);
        }catch (Exception e){
            Assertions.assertEquals("User not found", e.getMessage());
        }

    }
    @DisplayName("User: Successfully update")
    @Test
    void updateUser() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<Object> userReturn = userController.updateUser(user, id);

        Assertions.assertEquals("user1",  ((User) userReturn.getBody()).getName());
        Assertions.assertTrue( (user.getBirthdate().isEqual(((User)userReturn.getBody()).getBirthdate())) );
        Assertions.assertEquals(HttpStatus.OK, userReturn.getStatusCode());

    }

    @DisplayName("User: List of users found successfully")
    @Test
    void findAll() {
        User user2 = new User("user2", "user2", LocalDate.of(2020,02,02), new ArrayList<Book>());
        user2.setId(2);
        List<User> users = new ArrayList<User>();
        users.add(user);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        ResponseEntity<Object> userReturn = userController.findAll();
        Assertions.assertEquals("user1", ((List<User>) userReturn.getBody()).get(0).getUsername());
        Assertions.assertEquals("user2", ((List<User>) userReturn.getBody()).get(1).getName());
        Assertions.assertEquals(HttpStatus.OK, userReturn.getStatusCode());
    }

    @DisplayName("User: User found successfully")
    @Test
    void findOne() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        userController.findOne(id);
        ResponseEntity<Object> userReturn = userController.findOne(id);
        Assertions.assertEquals("user1", ((User) userReturn.getBody()).getUsername());
        Assertions.assertEquals(HttpStatus.OK, userReturn.getStatusCode());
    }

    @DisplayName("User: Book added successfully")
    @Test
    void addBook() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<Object> userReturn = userController.addBook(book, id);
        Assertions.assertEquals(book, ((User) userReturn.getBody()).getBooks().get(0));
    }

    @DisplayName("User: Exception when adding book")
    @Test
    void addBookInvalid() {
        long id = 1;
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(bookRepository.findById(id)).thenThrow(new BookNotFoundException("Book not found"));
        try {
            userController.addBook(book, id);
        }catch (Exception e){
            Assertions.assertEquals("Book not found", e.getMessage());
        }
    }

    @DisplayName("User: Book removed successfully")
    @Test
    void removeBook() {
        long id = 1;
        user.addBook(book);
        when(userRepository.findById(id)).thenReturn(Optional. of(user));
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(userRepository.save(user)).thenReturn(user);
        ResponseEntity<Object> userReturn = userController.removeBook(book, id);
        Assertions.assertTrue(((User) userReturn.getBody()).getBooks().isEmpty());
    }
}