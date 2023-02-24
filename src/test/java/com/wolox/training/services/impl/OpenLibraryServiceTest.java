package com.wolox.training.services.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class OpenLibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OpenLibraryService openLibraryService;

    private BookDto bookDto;

    private Book book;

    WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        List<AuthorDto> authorsDto = new ArrayList<AuthorDto>();
        authorsDto.add(new AuthorDto( "Tsai Chih Chung"));
        List<PublishersDto> publishersDto = new ArrayList<PublishersDto>();
        publishersDto.add(new PublishersDto("Anchor"));
        bookDto =  new BookDto( "OL7440033M", "Zen Speaks", "Shouts of Nothingness",publishersDto, "April 15, 1994" , 160, authorsDto);

        List<String> authors = new ArrayList<String>();
        authors.add("Tsai Chih Chung");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Anchor");
        book =  new Book("Terror", authors, "abcd", "Zen Speaks", "Shouts of Nothingness",
                publishers, "April 15, 1994", 160, "OL7440033M");
        wireMockServer = new WireMockServer(options().port(8080));
        wireMockServer.start();
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @DisplayName("findInExternalApi_whenExecutedBookInfo_thenReturnBookDto")
    @Test
    void bookInfoExternal() throws Exception{
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        BookDto bookDtoExternal = openLibraryService.bookInfo(bookDto.getIsbn());
        assertEquals(bookDto.getIsbn(),  bookDtoExternal.getIsbn());
        assertEquals(bookDto.getSubtitle(),  bookDtoExternal.getSubtitle());
        assertEquals(bookDto.getPublishDate(),  bookDtoExternal.getPublishDate());
    }

    @DisplayName("findInDb_whenExecutedBookInfoDb_thenReturnBookDto")
    @Test
    void bookInfoDb() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(book));
        BookDto bookDtoExternal = openLibraryService.bookInfo(bookDto.getIsbn());
        assertEquals(bookDto.getIsbn(),  bookDtoExternal.getIsbn());
    }

    @Test
    void getBookByIsbn() throws Exception {

        stubFor(get("books/OL7353617M.json")
                        .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("resource/response_ok_books.json")));

        BookDto bookDtoRes = openLibraryService.getBookByIsbn("OL7353617M");

        Assertions.assertEquals(bookDtoRes.getTitle(), "Fantastic Mr. Fox");
        Assertions.assertEquals(bookDtoRes.getIsbn(), "OL7353617M");
    }

    @DisplayName("whenTheBookIsNotFoundInTheExternalApiItThrowsException")
    @Test
    void getBookByIsbnInvalid() throws Exception {
        stubFor(get("books/OL7353617M.json")
                .willReturn(notFound()));

        Assertions.assertThrows(BookNotFoundException.class, () -> {
            openLibraryService.getBookByIsbn("XXXXX");
        });
    }

    @Test
    void getAuthosrByKey() throws Exception{
        stubFor(get("/authors/OL34184A.json")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("resource/response_ok_authors.json")));

        AuthorDto authorDtoRes = openLibraryService.getAuthosrByKey("/authors/OL34184A");

        Assertions.assertEquals(authorDtoRes.getName(), "Roald Dahl");
        Assertions.assertEquals(authorDtoRes.getUrl(), "/authors/OL34184A");
    }
}