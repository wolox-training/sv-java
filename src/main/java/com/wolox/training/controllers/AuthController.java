package com.wolox.training.controllers;

import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "See logged user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user logged", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not logged", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<Object> loggedUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);

        return new ResponseEntity<>( user, HttpStatus.OK);
    }
}
