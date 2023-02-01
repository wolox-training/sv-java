package com.wolox.training.models;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

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
    @NonNull
    private String username;

    @NonNull
    private String name;

    @NonNull
    private LocalDate birthdate;

    @NonNull
    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Book> books;

    public User() {
    }

    public User(long id, @NonNull String username, @NonNull String name, @NonNull LocalDate birthdate, @NonNull List<Book> books) {
        this.id = id;
        username = username;
        name = name;
        birthdate = birthdate;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        username = username;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        name = name;
    }

    @NonNull
    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(@NonNull LocalDate birthdate) {
        birthdate = birthdate;
    }

    @NonNull
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
