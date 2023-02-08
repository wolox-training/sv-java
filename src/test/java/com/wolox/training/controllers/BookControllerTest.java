package com.wolox.training.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.BookRepeatedTitleException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;

    @BeforeEach
    void setUp(){
        book =  new Book("Terror", "Stephen King", "abcd", "IT", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
    }

    @DisplayName("User load in the db correct parameters")
    @Test
    void create() throws Exception{
        when(bookRepository.save(book)).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.post("/api/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("Book: Successfully delete book")
    @Test
    void delete() throws Exception{
       long id = 1;
        when((bookRepository.findById(id))).thenReturn(Optional. of(book));
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @DisplayName("Book: Successfully update")
    @Test
    void updateBook() throws Exception{
        long id = 1;
        book.setId(id);
        Book book2 =  new Book("Terror", "Stephen King", "abcd", "IT2", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book2);
        mvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book2.getTitle()))
                .andDo(print());

    }

    @DisplayName("Book: List of books found successfully")
    @Test
    void findAll()throws Exception {
        Book book2 = new Book("Terror", "Stephen King", "abcd", "IT2", "Tu tambien flotaras",
                "Viking", "1986", 1504, "abcd");
        book2.setId(2);
        book.setId(1);
        List<Book> books = new ArrayList<>(Arrays.asList(book, book2));
        when(bookRepository.findAll()).thenReturn(books);
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();

        mvc.perform(MockMvcRequestBuilders.get("/api/books").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(books.size()))
                .andDo(print());
    }

    @DisplayName("Book: Book found successfully")
    @Test
    void findOne() throws Exception{
        long id = 1;
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        mvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andDo(print());
    }
}