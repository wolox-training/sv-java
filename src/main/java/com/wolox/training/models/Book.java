package com.wolox.training.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * This class is used to define a book
 * @author Varela Susana
 * @version 1.0
 */
@Entity
@Table(name = "books")
public class Book {

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

    public Book() {
    }

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

    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public float getPages() {
        return pages;
    }

    public void setPages(float pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
