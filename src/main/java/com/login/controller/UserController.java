package com.login.controller;


import com.login.dto.UserDTO;
import com.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/{login}")
    public ResponseEntity<?> getUser(@PathVariable("login") String login) {
        final String errorBody = "Login " + login + " not found";

        try {
            UserDTO user = userService.login(login);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (HttpClientErrorException exception) {
            return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
        }
    }
}
