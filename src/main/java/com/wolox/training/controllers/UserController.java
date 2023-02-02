package com.wolox.training.controllers;

import com.wolox.training.exceptions.BookIdMismatchException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.BookRepeatedTitleException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.exceptions.UserUsernameRepeatedException;
import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * save the received user
     * @param user user to be saved
     * @exception UserUsernameRepeatedException when the username is already in the database.
     * @return the user saved
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody User user) {
        System.out.println(user.getName());
        if(userRepository.findByUsername(user.getUsername()) != null){
            throw new UserUsernameRepeatedException();
        }
        return new ResponseEntity<>( userRepository.save(user), HttpStatus.CREATED);
    }

    /**
     * delete user searched by id
     * @param id to find user
     * @exception UserNotFoundException when user was not found
     * @return String is a descriptive text
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
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
     * @exception BookIdMismatchException if user does not match the id, modified message
     * @exception UserUsernameRepeatedException when the username is already in the database.
     * @exception UserNotFoundException when user was not found
     * @return user updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new BookIdMismatchException("The id does not match the request");
        }
        if(userRepository.findByUsername(user.getUsername()) != null){
            throw new UserUsernameRepeatedException();
        }
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return new ResponseEntity<>( userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findOne(@PathVariable Long id) {
        return new ResponseEntity<>(userRepository.findById(id).orElseThrow(UserNotFoundException::new), HttpStatus.OK);
    }

    //agregar libro
    //quitar libro
}
