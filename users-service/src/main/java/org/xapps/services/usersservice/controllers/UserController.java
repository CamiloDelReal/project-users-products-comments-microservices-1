package org.xapps.services.usersservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.services.usersservice.dtos.UserRequest;
import org.xapps.services.usersservice.dtos.UserResponse;
import org.xapps.services.usersservice.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String test() {
        return "dfsdfsdf";
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('Administrator') or principal.id == #id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        UserResponse userResponse = userService.getById(id);
        ResponseEntity<UserResponse> response;
        if(userResponse != null) {
            response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.create(userRequest);
        ResponseEntity<UserResponse> response;
        if(userResponse != null) {
            response = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
