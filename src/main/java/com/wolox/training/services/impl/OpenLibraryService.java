package com.wolox.training.services.impl;

import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.IOpenLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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


    public BookDto getBookByIsbn(String isbn) throws BookNotFoundException{
        String url = "books/"+isbn+".json";

        try {
            BookDto dto = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(BookDto.class).block();
            dto.setIsbn(isbn);
            List<AuthorDto> authorDtos = dto.getAuthors();
            authorDtos.forEach(author -> author.setName(getAuthosrByKey(author.getUrl()).getName()));
            dto.setAuthors(authorDtos);
            return dto;
        }  catch (WebClientResponseException wcre) {

            throw new BookNotFoundException();
        } catch (Exception ex) {
            throw new BookNotFoundException();
        }

    }

    public AuthorDto getAuthosrByKey(String key) {

        String url = key + ".json";
        try {
            AuthorDto dto = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(AuthorDto.class).block();
            return dto;
        }  catch (WebClientResponseException wcre) {

            throw new BookNotFoundException();
        } catch (Exception ex) {
            throw new BookNotFoundException();
        }
    }

    public BookDto passageBookToDto(Book book) {
       BookDto dto = new BookDto();
       dto.setIsbn(book.getIsbn());
       dto.setTitle(book.getTitle());
       dto.setSubtitle(book.getSubtitle());
       dto.setNumberOfPages(book.getPages());
       dto.setPublishDate(book.getYears());
       List<PublishersDto> publishersDto = new ArrayList<>();
       book.getPublishers().forEach(publisher -> publishersDto.add(new PublishersDto(publisher)));
       dto.setPublishers(publishersDto);

       List<AuthorDto> authorDto = new ArrayList<>();
       book.getAuthors().forEach(author -> authorDto.add(new AuthorDto(author)));
       dto.setAuthors(authorDto);
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
        }else{
            dto = getBookByIsbn(isbn);
            bookRepository.save(passageDtoToBook(dto));
        }
        return dto;
    }

}
