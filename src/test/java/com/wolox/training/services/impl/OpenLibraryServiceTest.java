package com.wolox.training.services.impl;

import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenLibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OpenLibraryService openLibraryService;

    private BookDto bookDto;

    private Book book;


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
    }


    @DisplayName("findInExternalApi_whenExecutedBookInfo_thenReturnBookDto")
    @Test
    void bookInfoExternal() throws Exception{
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        BookDto bookDtoExternal = openLibraryService.bookInfo(bookDto.getIsbn());
        Assertions.assertEquals(bookDto.getIsbn(),  bookDtoExternal.getIsbn());
        Assertions.assertEquals(bookDto.getSubtitle(),  bookDtoExternal.getSubtitle());
        Assertions.assertEquals(bookDto.getPublishDate(),  bookDtoExternal.getPublishDate());
    }

    @DisplayName("findInDb_whenExecutedBookInfoDb_thenReturnBookDto")
    @Test
    void bookInfoDb() {
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(book));
        BookDto bookDtoExternal = openLibraryService.bookInfo(bookDto.getIsbn());
        Assertions.assertEquals(bookDto.getIsbn(),  bookDtoExternal.getIsbn());
    }
}