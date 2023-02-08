package com.wolox.training.services.impl;

import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.IOpenLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("OpenLibraryService")
public class OpenLibraryService implements IOpenLibraryService {

    private final WebClient webClient;

    @Autowired
    private BookRepository bookRepository;

    public OpenLibraryService(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://openlibrary.org/").build();
    }


    public BookDto getBookByIsbn(String isbn) {
        isbn = "OL7440033M";

        String url = "books/"+isbn+".json";
        BookDto dto = webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(BookDto.class).block();
        dto.setIsbn(isbn);
        System.out.println("dto externo : " + dto.toString());

        return dto;
    }

    public BookDto passageBookToDto(Book book) {
        BookDto dto = new BookDto();
       dto.setIsbn(book.getIsbn());
       dto.setTitle(book.getTitle());
       dto.setSubtitle(book.getSubtitle());
       dto.setNumberOfPages(book.getPages());
        book.getPublishers().forEach(publisher ->
                dto.getPublishers().add(new PublishersDto(publisher)));
       dto.setPublishDate(book.getYears());
      book.getAuthors().forEach(author ->
              dto.getAuthors().add(new AuthorDto(author)));

        return dto;
    }

    public Book passageDtoToBook(BookDto dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setSubtitle(dto.getSubtitle());
        book.setPages(dto.getNumberOfPages());
        book.setYears(dto.getPublishDate());

        List<String> publishersList = new ArrayList<>();
        dto.getPublishers().forEach(publisher -> publishersList.add(publisher.getName()));
        book.setPublishers(publishersList);

        List<String> authorsList = new ArrayList<>();
        dto.getAuthors().forEach(author -> authorsList.add(author.getName()));
        book.setAuthors(authorsList);

        book.setGenre("none");
        book.setImage("none");

        return book;
    }

    @Override
    public BookDto bookInfo(String isbn) {
        BookDto dto = new BookDto();
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if(book.isPresent()){
            dto = passageBookToDto(book.get());
            System.out.println("dto repo : " + dto.toString());
        }else{
            dto = getBookByIsbn(isbn);
           bookRepository.save(passageDtoToBook(dto));
        }
        return dto;
    }

}
