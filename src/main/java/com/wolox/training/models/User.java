package com.wolox.training.models;

import com.wolox.training.exceptions.BookAlreadyOwnedException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty
    private String username;

    @NotEmpty
    private String name;

    @Past
    @NotEmpty
    private LocalDate birthdate;

    @ManyToMany(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Book> books;

    public User() {
    }

    public User(long id, String username, String name, LocalDate birthdate, List<Book> books) {
        this.id = id;
        username = username;
        name = name;
        birthdate = birthdate;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }


    public List<Book> addBook(Book book){
        if(this.books.indexOf(book) != -1){
            this.books.add(book);
        }else{
            throw new BookAlreadyOwnedException();
        }
        return this.books;
    }

    public boolean removeBook(Book book){
        int pos = this.books.indexOf(book);
        boolean removed = false;
        if(pos != -1){
            this.books.remove(pos);
            removed = true;
        }
        return removed;
    }
}
