package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp(){
        book =  new Book("Terror", "Stephen King", "abcd", "IT", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
        bookRepository.save(book);
    }

    @DisplayName("whenExecutedSave_savesTheBookAndGeneratesItsId")
    @Test
    void save() {
        Book savebook =  new Book("Terror", "Stephen King", "abcd", "IT2", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
        bookRepository.save(savebook);
        long id = 2;
        Optional<Book> savedBook = bookRepository.findById(id);
        Assertions.assertEquals(savebook.getGenre(), savedBook.get().getGenre());

    }

    @DisplayName("whenExecutedFindByTitle_returnTheRequestedBook")
    @Test
    void findByTitle() {
        Optional<Book> savedBook = bookRepository.findByTitle(book.getTitle());
        Assertions.assertTrue(savedBook.isPresent());
        Assertions.assertTrue(savedBook.get().getId() > 0);

    }

    @DisplayName("whenExecutedFindById_returnTheRequestedBook")
    @Test
    void findById(){
        long id = 1;
        Optional<Book> savedBook = bookRepository.findById(id);
        Assertions.assertEquals(book.getGenre(), savedBook.get().getGenre());
    }


}