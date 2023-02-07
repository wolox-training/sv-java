package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        List<Book> books = new ArrayList<Book>();
        user = new User("user1", "user1", LocalDate.of(2022,02,02), books);
        userRepository.save(user);
    }

    @DisplayName("User: User saved successfully")
    @Test
    void save() {
        List<Book> books = new ArrayList<Book>();
        User saveUser = new User("user2", "user2", LocalDate.of(2020,02,02), books);
        userRepository.save(saveUser);
        long id = 2;
        Optional<User> savedUser = userRepository.findById(id);
        Assertions.assertEquals("user2", savedUser.get().getUsername());
        Assertions.assertTrue(savedUser.get().getId() == id);

    }

    @DisplayName("User: User find by username found successfully")
    @Test
    void findByUsername() {
        Optional<User> savedUser = userRepository.findByUsername(user.getUsername());
        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertTrue(savedUser.get().getId() > 0);
    }

    @DisplayName("User: User find by id found successfully")
    @Test
    void findById(){
        long id = 1;
        Optional<User> savedUser = userRepository.findById(id);
        Assertions.assertEquals(user.getName(), savedUser.get().getName());
    }
}