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

    long id ;

    @BeforeEach
    public void setUp(){
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        book =  new Book("Terror", authors, "abcd", "IT", "Tu tambien flotaras",
                publishers, "1986", 1504, "abcd");
        id = bookRepository.save(book).getId();

        Book savebook2 =  new Book("Terror", authors, "abcd", "IT3", "subtitle",
                publishers, "2023", 1000, "abcd");
        Book savebook3 =  new Book("Terror", authors, "abcd", "IT4", "Tu tambien flotaras",
                publishers, "1986", 1504, "abcd");
        Book savedBook2 =  bookRepository.save(savebook2);
        Book savedBook3 =  bookRepository.save(savebook3);
        List<Book> books = new ArrayList<Book>();
        books.add(savedBook2);
        books.add(savedBook3);
        books.add(book);
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

        Optional<Book> savedBook = bookRepository.findById(id);
        Assertions.assertEquals(book.getGenre(), savedBook.get().getGenre());
    }

    @DisplayName("whenExecutedFindByEditorialPublisherAndGenreAndYears_returnsTheBooksThatMatchTheParameters")
    @Test
    void findByEditorialPublisherAndGenreAndYears(){
        String genre = "Terror";
        String publisher = "Viking";
        String years = "1986";
        List<Book> books = bookRepository.findByPublisherAndGenreAndYears(publisher, genre, years);
        Assertions.assertEquals(2, books.size());

    }


    @DisplayName("whenExecutedFindByPublisherGenreYears_returnsTheBooksThatMatchTheParametersOrtheParametersAreNull")
    @Test
    void findByPublisher_Genre_Years(){
        String genre = "Terror";
        String years = "1986";
        List<Book> books = bookRepository.findByPublisher_Genre_Years(null, genre, years);
        Assertions.assertEquals(2, books.size());

    }

    @DisplayName("whenExecutedFindByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn_returnsTheBooksThatMatchTheParametersOrtheParametersAreNull")
    @Test
    void findByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn(){
        String publisher  =   "viking";
        String genre = "terror";
        String years = "1986";
        String pages = "1504";
        List<Book> books = bookRepository.findByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn(publisher, genre, years, null, null, null,
                null, pages, null);
        Assertions.assertEquals(2, books.size());

   }

}