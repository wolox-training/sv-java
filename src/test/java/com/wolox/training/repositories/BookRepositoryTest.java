package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp(){
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        book =  new Book("Terror", authors, "abcd", "IT", "Tu tambien flotaras",
                publishers, "1986", 1504, "abcd");
        bookRepository.save(book);
    }

    @DisplayName("whenExecutedSave_savesTheBookAndGeneratesItsId")
    @Test
    void save() {
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        Book savebook =  new Book("Terror", authors, "abcd", "IT2", "Tu tambien flotaras",
                publishers, "1986", 1504, "abcd");

        Book savedBook =  bookRepository.save(savebook);
        Optional<Book> bookById = bookRepository.findById(savedBook.getId());
        Assertions.assertEquals(savebook.getGenre(), bookById.get().getGenre());

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