package com.wolox.training.services.impl;

import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.IOpenLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("OpenLibraryService")
public class OpenLibraryService implements IOpenLibraryService {

    private WebClient webClient;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Find an external api for the requested book
     * @param isbn is the key to the search
     * @return return a BookDto with the loaded data
     * @throws BookNotFoundException when the book was not found
     */
    public BookDto getBookByIsbn(String isbn) throws BookNotFoundException{
        String url = "books/"+isbn+".json";
         webClient = WebClient.builder()
                .baseUrl("https://openlibrary.org/")
                .build();

            BookDto dto = webClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .onStatus(s -> s.equals(HttpStatus.NOT_FOUND), (e) -> Mono.error(new BookNotFoundException()))
                    .bodyToMono(BookDto.class).block();
            dto.setIsbn(isbn);
            List<AuthorDto> authorDtos = dto.getAuthors();
            authorDtos.forEach(author -> author.setName(getAuthosrByKey(author.getUrl()).getName()));
            dto.setAuthors(authorDtos);
            return dto;


    }

    /**
     * Find an external API for the requested author
     * @param key is the key to the search
     * @return return a AuthorDto with the loaded data
     */
    public AuthorDto getAuthosrByKey(String key) {
         webClient = WebClient.builder()
                .baseUrl("https://openlibrary.org/")
                .build();

        String url = key + ".json";
        AuthorDto dto = webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(s -> s.equals(HttpStatus.NOT_FOUND), (e) -> Mono.error(new BookNotFoundException()))
                .bodyToMono(AuthorDto.class).block();
            return dto;
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

    /**
     * Finds a requested book, if it is not in the database it finds it in an external API saving it in the database
     * @param isbn is the key to the search
     * @return return a BookDto with the loaded data
     */
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
