package com.wolox.training.controllers;

import com.wolox.training.dtos.BookDto;
import com.wolox.training.exceptions.ObjectIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.BookRepeatedTitleException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.IOpenLibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IOpenLibraryService iOpenLibraryService;

    /**
     * save the received book
     * @param book book to be saved
     * @exception BookRepeatedTitleException when the book title is already in the database.
     * @return the book saved
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object>  create(@RequestBody Book book) {
        if(!bookRepository.findByTitle(book.getTitle()).isEmpty()){
            throw new BookRepeatedTitleException();
        }
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    /**
     * delete the book searched by id
     * @param id to find the book
     * @exception BookNotFoundException when the book was not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
        return new ResponseEntity<>("The book was removed", HttpStatus.OK);
    }

    /**
     * update the book in the database with the received book
     * @param book to be updated
     * @param id to search for the book to update
     * @exception ObjectIdMismatchException if the book does not match the id
     * @exception BookNotFoundException when the book was not found
     * @return the book updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new ObjectIdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return  new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    /**
     * find for a book by id
     * @param id to search for the book
     * @exception  BookNotFoundException when the book was not found
     * @return the found book
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable Long id) {
        return new ResponseEntity<>(bookRepository.findById(id).orElseThrow(BookNotFoundException::new), HttpStatus.OK);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * Finds a requested book if it is not in the database finds for it in an external api
     * @param isbn is the key to the search
     * @return return a BookDto with the loaded data and If it finds it in the database, it sends in ok.
     *         If it does not find it, looks for it in an external api, returns a bookdto and created code.
     *         If it does not find it returns not found.
     */
    @GetMapping("/isbn={isbn}")
    public ResponseEntity<Object> findByIsbn(@PathVariable String isbn) {
        BookDto bookDto = new BookDto();

        HttpStatus httpStatus = bookRepository.findByIsbn(isbn).isPresent() ? HttpStatus.OK : HttpStatus.CREATED;
        bookDto = iOpenLibraryService.bookInfo(isbn);
        return new ResponseEntity<>(bookDto, httpStatus);
    }

    /**
     * Searches for a list of books with the specified parameters and can be null,
     * the pagination data is by default and can be modified
     * @param allParams contains the parameters for the search in the database
     * @param pageable contains the parameters to perform the pagination
     * @return a Page with the list of books
     */
    @Operation(summary = "List all books by parameters, can receive null parameters")
    @ApiResponse(responseCode = "200", description = "All books successfully", content = @Content)
    @GetMapping("/getall/")
    public ResponseEntity<Object> getAll(@RequestParam Map<String,String> allParams, @PageableDefault(page = 0, size = 20)
    @SortDefault.SortDefaults({@SortDefault(sort = "title", direction = Sort.Direction.ASC)})
            Pageable pageable){

        String publisher  =  allParams.get("publishers") ;
        String genre = allParams.get("genre");
        String years = allParams.get("years");
        String authors = allParams.get("authors");
        String image = allParams.get("image");
        String title = allParams.get("title");
        String subtitle = allParams.get("subtitle");
        String pages = allParams.get("pages");
        String isbn = allParams.get("isbn");

        Page<Book> books = bookRepository.findByPublisherGenreYearsAuthorsImageTitleSubtitlePagesIsbn(publisher, genre, years, authors, image, title,
        subtitle, pages, isbn, pageable);

        if(books.getContent().isEmpty()){
            throw new BookNotFoundException();
        }

        return new ResponseEntity<>( books, HttpStatus.OK);
    }
}
