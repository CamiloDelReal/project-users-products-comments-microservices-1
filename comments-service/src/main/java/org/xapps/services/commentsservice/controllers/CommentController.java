package org.xapps.services.commentsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.xapps.services.commentsservice.dtos.CommentCreateRequest;
import org.xapps.services.commentsservice.dtos.CommentEditRequest;
import org.xapps.services.commentsservice.dtos.CommentResponse;
import org.xapps.services.commentsservice.dtos.UserAuthenticated;
import org.xapps.services.commentsservice.services.CommentService;

import javax.validation.Valid;
import java.util.List;


@RestController
public class CommentController {

    private CommentService commentService;
    private AuthenticationManager authManager;

    @Autowired
    public CommentController(CommentService commentService, AuthenticationManager authManager) {
        this.commentService = commentService;
        this.authManager = authManager;
    }

    @GetMapping(path = "/users/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<List<CommentResponse>> getAllByUserId(@PathVariable("id") Long id) {
        List<CommentResponse> commentsResponse = commentService.getAllByUserId(id);
        return new ResponseEntity<>(commentsResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/products/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponse>> getAllByProductId(@PathVariable("id") Long id) {
        List<CommentResponse> commentsResponse = commentService.getAllByProductId(id);
        return new ResponseEntity<>(commentsResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/users/{userId}/products/{productId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #id")
    public ResponseEntity<List<CommentResponse>> getAllByUserIdAndProductId(@PathVariable("userId") Long userId, @PathVariable("productId") Long productId) {
        List<CommentResponse> commentsResponse = commentService.getAllByUserIdAndProductId(userId, productId);
        return new ResponseEntity<>(commentsResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator') or isAuthenticated() and principal.id == #request.userId")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateRequest request) {
        CommentResponse commentResponse = commentService.create(request);
        ResponseEntity<CommentResponse> response;
        if(commentResponse != null) {
            response = new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return response;
    }

    @PutMapping(path = "/comments/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> editComment(@PathVariable("id") Long id, @Valid @RequestBody CommentEditRequest request) {
        ResponseEntity<CommentResponse> response;
        UserAuthenticated user = (UserAuthenticated)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.hasRole("Administrator") || commentService.isOwner(id, user.getId())) {
            CommentResponse commentResponse = commentService.edit(id, request);
            if(commentResponse != null) {
                response = new ResponseEntity<>(commentResponse, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @DeleteMapping(path = "/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        ResponseEntity<Void> response;
        UserAuthenticated user = (UserAuthenticated)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.hasRole("Administrator") || commentService.isOwner(id, user.getId())) {
            boolean success = commentService.delete(id);
            if (success) {
                response = new ResponseEntity<>(HttpStatus.OK);
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

}
