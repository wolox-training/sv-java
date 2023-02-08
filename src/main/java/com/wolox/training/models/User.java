package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to define a user
 * @author Varela Susana
 * @version 1.0
 */
@Schema(description = "user with their book list")
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Schema(name = "username")
    @Column(nullable = false, unique = true)
    @NotBlank
    private String username;

    @Schema(name ="name")
    @Column(nullable = false)
    @NotBlank
    private String name;

    @Schema(name ="birthdate dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(nullable = false)
    @NotNull
    private LocalDate birthdate;

    @Schema(name ="books")
    @ManyToMany(cascade= {CascadeType.REFRESH, CascadeType.MERGE} ,fetch= FetchType.LAZY)
    private List<Book> books;

    public User() {
    }

    public User(String username, String name, LocalDate birthdate, List<Book> books) {
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    /**
     * add the received book to the list
     * @param book book to be added
     * @exception BookAlreadyOwnedException when the book is already on the list
     * @return list with added book
     */
    public List<Book> addBook(Book book){
        if(this.books.indexOf(book) == -1){
            this.books.add(book);
        }else{
            throw new BookAlreadyOwnedException();
        }
        return this.books;
    }

    /**
     * search and remove the book from the list
     * @param book book to remove from list
     * @return boolean true if it was removed or false if not found
     */
    public List<Book> removeBook(Book book){
        int pos = this.books.indexOf(book);
        if(pos != -1){
            this.books.remove(pos);
        }else{
            throw new BookNotFoundException();
        }
        return this.books;
    }
}
