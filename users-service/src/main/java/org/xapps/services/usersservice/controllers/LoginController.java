package org.xapps.services.usersservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xapps.services.usersservice.dtos.LoginRequest;
import org.xapps.services.usersservice.dtos.LoginResponse;
import org.xapps.services.usersservice.services.UserService;


@RestController
@RequestMapping(path = "/security")
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.authenticate(loginRequest);
        ResponseEntity<LoginResponse> response;
        if(loginResponse != null) {
            response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return response;
    }
}
