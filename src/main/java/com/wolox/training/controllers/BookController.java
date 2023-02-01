package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.BookRepeatedTitleException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * save the received book
     * @param book book to be saved
     * @exception BookRepeatedTitleException when the book title is already in the database.
     * @return the book saved
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        if(!bookRepository.findByTitle(book.getTitle()).isEmpty()){
            throw new BookRepeatedTitleException();
        }
        return bookRepository.save(book);
    }

    /**
     * delete the book searched by id
     * @param id to find the book
     * @exception BookNotFoundException when the book was not found
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * update the book in the database with the received book
     * @param book to be updated
     * @param id to search for the book to update
     * @exception BookIdMismatchException if the book does not match the id
     * @exception BookNotFoundException when the book was not found
     * @return the book updated
     */
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    @GetMapping
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    /**
     * find for a book by id
     * @param id to search for the book
     * @exception  BookNotFoundException when the book was not found
     * @return the found book
     */
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
