package com.wolox.training.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This class is used to define a book
 * @author Varela Susana
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "books")
public class Book {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @NotNull
    private String genre;

    @Column(nullable = false)
    @NotNull
    private List<String> authors;

    @Column(nullable = false)
    @NotNull
    private String image;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotNull
    private String subtitle;

    @Column(nullable = false)
    @NotNull
    private List<String> publishers;

    @Column(nullable = false)
    @NotNull
    private String years;

    @Column(nullable = false)
    @NotNull
    private float pages;

    @Column(nullable = false)
    @NotNull
    private String isbn;

    public Book(String genre,  List<String> authors, String image, String title, String subtitle, List<String> publishers, String years, float pages, String isbn) {
        this.genre = genre;
        this.authors = authors;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publishers = publishers;
        this.years = years;
        this.pages = pages;
        this.isbn = isbn;
    }
}
