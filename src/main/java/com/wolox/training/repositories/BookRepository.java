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


    @Query(value = "select * from books b where (b.genre=:genre or :genre is null) and (b.years =:years or :years is null)" +
            " and (b.publishers like %:publisher% or :publisher is null)", nativeQuery = true)
    List<Book> findByPublisher_Genre_Years(@Param("publisher")String publisher,
                                               @Param("genre")String genre,
                                               @Param("years")String years);

}
