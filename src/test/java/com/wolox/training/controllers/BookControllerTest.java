package com.wolox.training.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.training.dtos.AuthorDto;
import com.wolox.training.dtos.BookDto;
import com.wolox.training.dtos.PublishersDto;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.IOpenLibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private IOpenLibraryService iOpenLibraryService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;

    private BookDto bookDto;

    @BeforeEach
    void setUp(){
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        book =  new Book("Terror", authors, "abcd", "IT", "Tu tambien flotaras",
                publishers, "1986", 1504, "abcd");

        List<AuthorDto> authorsDto = new ArrayList<AuthorDto>();
        authorsDto.add(new AuthorDto("Stephen King"));
        List<PublishersDto> publishersDto = new ArrayList<PublishersDto>();
        publishersDto.add(new PublishersDto("Viking"));
        bookDto =  new BookDto( "OL7440033M", "IT", "Tu tambien flotaras",publishersDto, "1986" , 1504, authorsDto);
    }

    @DisplayName("whenTheEndpointIsExecutedCreate_ReturnCreate")
    @Test
    void create() throws Exception{
        when(bookRepository.save(book)).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.post("/api/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("whenTheEndpointIsExecutedDelete_ReturnOk")
    @Test
    void delete() throws Exception{
       long id = 1;
        when((bookRepository.findById(id))).thenReturn(Optional. of(book));
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @DisplayName("whenTheEndpointIsExecutedUpdate_ReturnOkAndTheModifiedAttributesSuccessfully")
    @Test
    void updateBook() throws Exception{
        long id = 1;
        ReflectionTestUtils.setField(book, "id", id);
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        Book book2 =  new Book("Terror", authors, "abcd", "IT2", "Tu tambien flotaras",
                publishers, "1990", 1400, "abcd");
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book2);
        mvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book2.getTitle()))
                .andExpect(jsonPath("$.pages").value(book2.getPages()))
                .andExpect(jsonPath("$.years").value(book2.getYears()))
                .andDo(print());

    }

    @DisplayName("whenTheEndpointIsExecutedFindAll_ReturnOkAndTheNumberOfObjectsExpected")
    @Test
    void findAll()throws Exception {
        List<String> authors = new ArrayList<String>();
        authors.add("Stephen King");
        List<String> publishers = new ArrayList<String>();
        publishers.add("Viking");
        Book book2 = new Book("Terror", authors, "abcd", "IT2", "Tu tambien flotaras",
                publishers, "1990", 1400, "abcd");
        ReflectionTestUtils.setField(book2, "id", 2L);
        ReflectionTestUtils.setField(book, "id", 1L);
        List<Book> books = new ArrayList<>(Arrays.asList(book, book2));
        when(bookRepository.findAll()).thenReturn(books);
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();

        mvc.perform(MockMvcRequestBuilders.get("/api/books").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(books.size()))
                .andDo(print());
    }

    @DisplayName("whenTheEndpointIsExecutedFindOne_ReturnOkAndTheRequestedBooks")
    @Test
    void findOne() throws Exception{
        long id = 1;
        when(bookRepository.findById(id)).thenReturn(Optional. of(book));
        mvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.authors").value(book.getAuthors()))
                .andDo(print());
    }

    @DisplayName("sinceTheBookIsInTheDb_whenTheEndpointIsExecutedFindByIsbn_thenReturnOkAndBook")
    @Test
    void findByIsbn() throws Exception {
        String isbn = "OL7440033M";
        when(iOpenLibraryService.bookInfo(isbn)).thenReturn(bookDto);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        mvc.perform(MockMvcRequestBuilders.get("/api/books/isbn={isbn}", isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andDo(print());
    }

    @DisplayName("sinceTheBookIsNotInTheDb_whenTheEndpointIsExecutedFindByIsbn_thenReturnCreateAndBook")
    @Test
    void findByIsbnExternal() throws Exception {
        String isbn = "OL7440033M";
        when(iOpenLibraryService.bookInfo(isbn)).thenReturn(bookDto);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/api/books/isbn={isbn}", isbn))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andDo(print());
    }

    @DisplayName("NotFoundInExternalApi_whenTheEndpointIsExecutedFindByIsbn_thenReturnNotFoundAndBook")
    @Test
    void findByIsbnException() throws Exception {
        String isbn = "OL7440033M";
        when(iOpenLibraryService.bookInfo(isbn)).thenThrow(new BookNotFoundException());
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/api/books/isbn={isbn}", isbn))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @DisplayName("whenTheEndpointIsGetAllWithTheParametersToBeFetchedAndTheRestLoadedNull _ returnsTheBooksMatch")
    @Test
    void getAll() throws Exception{
        String publishers  = "Viking";
        String genre = "Terror";
        String sort = "id";

        Pageable pageable = PageRequest.of(0, 20, Sort.by(sort));

        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        Page<Book> page = new PageImpl<>(bookList, pageable, bookList.size());

        when(bookRepository.findByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn(publishers, genre, null, null, null, null,
                null, null, null, pageable)).thenReturn(page);
        mvc.perform(MockMvcRequestBuilders.get("/api/books/getall/")
                        .param("publishers",publishers)
                        .param("genre", genre)
                        .param("sort",sort))
                .andExpect(jsonPath("$.totalElements").value(page.getTotalElements()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}