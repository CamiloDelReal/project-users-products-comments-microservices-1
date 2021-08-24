package org.xapps.services.usersservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.services.usersservice.dtos.UserRequest;
import org.xapps.services.usersservice.dtos.UserResponse;
import org.xapps.services.usersservice.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        logger.info("Request for getUserById 2");
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
        logger.info("Request for createUser");
        UserResponse userResponse = userService.create(userRequest);
        ResponseEntity<UserResponse> response;
        if(userResponse != null) {
            response = new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest) {
        logger.info("Request for editUser");
        UserResponse userResponse = userService.edit(id, userRequest);
        ResponseEntity<UserResponse> response;
        if(userResponse != null) {
            response = new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        boolean success = userService.delete(id);
        ResponseEntity<Void> response;
        if(success) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

}
