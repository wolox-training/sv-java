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

    long id;

    @BeforeEach
    void setUp(){
        List<Book> books = new ArrayList<Book>();
        user = new User("user1", "user1", LocalDate.of(2022,02,02), books, "USER", "1234");
        id = userRepository.save(user).getId();
    }

    @DisplayName("whenExecutedSave_savesUserAndGeneratesItsId")
    @Test
    void save() {
        List<Book> books = new ArrayList<Book>();
        User saveUser = new User("user2", "user2", LocalDate.of(2020,02,02),  books, "USER", "1234");

        User savedUser = userRepository.save(saveUser);
        Optional<User> userById = userRepository.findById(savedUser.getId());
        Assertions.assertEquals(saveUser.getUsername(), userById.get().getUsername());
    }

    @DisplayName("whenExecutedFindByUsername_returnTheRequestedUser")
    @Test
    void findByUsername() {
        Optional<User> savedUser = userRepository.findByUsername(user.getUsername());
        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertTrue(savedUser.get().getId() > 0);
    }

    @DisplayName("whenExecutedFindById_returnTheRequestedUser")
    @Test
    void findById(){
        Optional<User> savedUser = userRepository.findById(id);
        Assertions.assertEquals(user.getName(), savedUser.get().getName());
    }

    @DisplayName("whenExecutedFindByBirthdayBetweenDatesAndCharactersInName_returnsUsersThatMatchTheParameters")
    @Test
    void findByBirthdayBetweenDatesAndCharactersInName(){
        User user2 = new User("user2", "Name", LocalDate.of(2023,02,02),new ArrayList<Book>(), "USER", "1234");
        userRepository.save(user2);

        String name = "name";
        LocalDate date1 = LocalDate.of(1987,01,01);
        LocalDate date2 = LocalDate.of(2024,01,01);
        List<User> users = userRepository.findByBirthdayBetweenDatesAndCharactersInName(name,date1, date2);
        Assertions.assertEquals(1, users.size());

    }


    @DisplayName("whenExecutedfindByBirthdateBetweenAndNameLikeIgnoreCase_returnsUsersThatMatchTheParameters")
    @Test
    void findByBirthdateBetweenAndNameLikeIgnoreCase(){
        User user2 = new User("user2", "Name", LocalDate.of(2023,02,02),new ArrayList<Book>(), "USER", "1234");
        userRepository.save(user2);

        String name = "name";
        LocalDate date1 = LocalDate.of(1987,01,01);
        LocalDate date2 = LocalDate.of(2024,01,01);
        List<User> users = userRepository.findByBirthdateBetweenAndNameLikeIgnoreCase(date1, date2,name);
        Assertions.assertEquals(1, users.size());

    }

    @DisplayName("whenExecutedFindByBirthdateBetweenAndNameLikeIgnoreCaseCanSendNullParameter_returnsUsersThatMatchTheParameters")
    @Test
    void findByBirthdateBetweenAndNameLikeIgnoreCaseOrNull(){
        User user2 = new User("user2", "Name", LocalDate.of(2023,02,02),new ArrayList<Book>(), "USER", "1234");
        userRepository.save(user2);

        String name = "name";
        LocalDate date2 = LocalDate.of(2024,01,01);
        List<User> users = userRepository.findUsersByBirthdateAndName(null, date2, name);
        Assertions.assertEquals(2, users.size());

    }
}