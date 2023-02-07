package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.BookRepeatedTitleException;
import com.wolox.training.exceptions.ObjectIdMismatchException;
import com.wolox.training.exceptions.RestExceptionHandler;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private MockMvc mvc;

    private Book book = new Book("Terror", "Stephen King", "abcd", "IT", "Tu tambien flotaras",
            "Viking", "1986", 1504, "abcd");

    @BeforeEach
    public void setUp(){

    }


    @Test
    void create() {
        book.setId(2);
        long id = 2;
        Optional<Book>  bookMock = Optional. of(book);
        when(bookRepository.save(book)).thenReturn(book);
        ResponseEntity<Object> bookReturn = bookController.create(book);
        Assertions.assertEquals("IT", ((Book) bookReturn.getBody()).getTitle());
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    void createInvalid() {
        Optional<Book>  bookMock = Optional. of(book);
        when(bookRepository.findByTitle("IT")).thenReturn(bookMock);
        Assertions.assertThrows(BookRepeatedTitleException.class, ()-> bookController.create(book), "the title is not valid");

    }

    @Test
    void delete() {
        book.setId(1);
        long id = 1;
        Optional<Book>  bookMock = Optional. of(book);
        when((bookRepository.findById(id))).thenReturn(bookMock);
        bookController.delete(id);
        Mockito.verify(bookRepository).deleteById(1L);

    }

    @Test
    void updateBook() {
        book.setId(1);
        long id = 1;

        Optional<Book>  bookMock = Optional. of(book);
        when(bookRepository.findById(id)).thenReturn(bookMock);
        when(bookRepository.save(book)).thenReturn(book);
        ResponseEntity<Object> bookReturn = bookController.updateBook(book, id);
        Assertions.assertEquals(1, ((Book) bookReturn.getBody()).getId());
        Assertions.assertEquals("IT", ((Book) bookReturn.getBody()).getTitle());
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    void findAll() {
        Book book2 = new Book("Terror", "Stephen King", "abcd", "IT2", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");;
        book2.setId(2);
        book.setId(1);
        List<Book> books = new ArrayList<Book>();
        books.add(book);
        books.add(book2);
        when(bookRepository.findAll()).thenReturn(books);
        ResponseEntity<Object> booksReturn = bookController.findAll();
        Assertions.assertEquals("IT", ((List<Book>) booksReturn.getBody()).get(0).getTitle());
    }

    @Test
    void findOne() {
        book.setId(1);
        Optional<Book>  bookMock = Optional. of(book);
        long id = 1;

        when(bookRepository.findById(id)).thenReturn(bookMock);
        bookController.findOne(id);
        Assertions.assertEquals(1,book.getId());
    }
}