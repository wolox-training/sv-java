package com.wolox.training.repositories;


import com.wolox.training.models.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    @Query(value = "select * from books b where b.genre=:genre and b.years =:years and b.publishers like %:publisher%", nativeQuery = true)
    List<Book> findByPublisherAndGenreAndYears(@Param("publisher")String publisher,
                                                        @Param("genre")String genre,
                                                        @Param("years")String years);

    @Query(value = "select * from books b where (LOWER(b.genre) like LOWER(concat('%', :genre, '%')) or :genre is null) and (b.years =:years or :years is null)" +
            " and (LOWER(b.publishers) LIKE LOWER(concat('%', :publisher, '%')) OR :publisher IS NULL)", nativeQuery = true)
    List<Book> findByPublisher_Genre_Years(@Param("publisher")String publisher,
                                               @Param("genre")String genre,
                                               @Param("years")String years);

    @Query(value = "select * from books b where (LOWER(b.genre) like LOWER(concat('%', :genre, '%')) or :genre is null) and (b.years =:years or :years is null)" +
            " and (LOWER(b.publishers) LIKE LOWER(concat('%', :publisher, '%')) OR :publisher IS NULL)" +
            " and (LOWER(b.authors) LIKE LOWER(concat('%', :authors, '%')) OR :authors IS NULL)" +
            "and (LOWER(b.image) like LOWER(concat('%', :image, '%')) or :genre is null) " +
            "and (LOWER(b.title) like LOWER(concat('%', :title, '%')) or :title is null)" +
            "and (LOWER(b.subtitle) like LOWER(concat('%', :subtitle, '%')) or :subtitle is null)" +
            "and (b.pages = :pages or :pages is null)" +
            "and (LOWER(b.isbn) like LOWER(concat('%', :isbn, '%')) or :isbn is null)", nativeQuery = true)
    List<Book> findByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn(@Param("publisher")String publisher,
                                           @Param("genre")String genre,
                                           @Param("years")String years,
                                           @Param("authors")String authors,
                                           @Param("image")String image,
                                           @Param("title")String title,
                                           @Param("subtitle")String subtitle,
                                           @Param("pages") String pages,
                                           @Param("isbn")String isbn);


}
