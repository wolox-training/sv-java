package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.exceptions.UserUsernameRepeatedException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * save the received user
     * @param user user to be saved
     * @exception UserUsernameRepeatedException when the username is already in the database.
     * @return the user saved
     */
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content),
            @ApiResponse(responseCode = "400", description = "User could not be created", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UserUsernameRepeatedException();
        }
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return new ResponseEntity<>( userRepository.save(user), HttpStatus.CREATED);
    }


    /**
     * delete user searched by id
     * @param id to find user
     * @exception UserNotFoundException when user was not found
     * @return String is a descriptive text
     */
   @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
      Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>( "User has been successfully removed", HttpStatus.OK);
        }else{
           throw new UserNotFoundException();
        }
    }

    /**
     * update user in the database with the received user
     * @param user to be updated
     * @param id to search for user to update
     * @exception UserUsernameRepeatedException when the username is already in the database.
     * @exception UserNotFoundException when user was not found
     * @return user updated
     */
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "If user does not match the id or the username is already registered", content = @Content)

    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable Long id) {
        User addUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if(!addUser.getUsername().equalsIgnoreCase(user.getUsername())){
            if(userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new UserUsernameRepeatedException();
            }
            addUser.setUsername(user.getUsername());
        }
        addUser.setBirthdate(user.getBirthdate());
        addUser.setName(user.getName());
        return new ResponseEntity<>(userRepository.save(addUser), HttpStatus.OK);
    }

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "All users successfully", content = @Content)
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>( userRepository.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Find a user identified by id")
    @ApiResponse(responseCode = "200", description = "Get user successfully", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable Long id) {
        return new ResponseEntity<>(userRepository.findById(id).orElseThrow(UserNotFoundException::new), HttpStatus.OK);
    }

    /**
     * add the received book to the list books
     * @param book  book to be added
     * @param id To find the user who owns the list
     * @exception UserNotFoundException when user was not found
     * @exception BookNotFoundException when the book was not found
     * @return user updated
     */
    @Operation(summary = "Add book to book list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or Book not found", content = @Content)
    })
    @PostMapping("addbook/{id}")
    public ResponseEntity<Object> addBook(@RequestBody Book book, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book addBook = bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);

        user.addBook(addBook);
        return new ResponseEntity<>( userRepository.save(user), HttpStatus.OK);
    }

    /**
     * Identical to the add endpoint but it removes the book
     */
    @Operation(summary = "Remove book to book list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book removed successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or Book not found", content = @Content)
    })
    @PostMapping("removebook/{id}")
    public ResponseEntity<Object> removeBook(@RequestBody Book book, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
       Book removeBook =bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);

        user.removeBook(removeBook);
        return new ResponseEntity<>( userRepository.save(user), HttpStatus.OK);
    }




}
