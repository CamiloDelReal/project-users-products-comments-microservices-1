package org.xapps.services.productsservice.controllers;

import brave.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.services.productsservice.dtos.CategoryRequest;
import org.xapps.services.productsservice.dtos.CategoryResponse;
import org.xapps.services.productsservice.services.CategoryService;

import javax.validation.Valid;
import javax.ws.rs.POST;
import java.util.List;


@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categories = categoryService.getAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> getById(@PathVariable("id") Long id) {
        CategoryResponse categoryResponse = categoryService.getById(id);
        ResponseEntity<CategoryResponse> response;
        if(categoryResponse != null) {
            response = new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.create(categoryRequest);
        ResponseEntity<CategoryResponse> response;
        if(categoryResponse != null) {
            response = new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
        } else {
            // Sql error (name duplicity for example)
            response = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<CategoryResponse> editCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.edit(id, categoryRequest);
        ResponseEntity<CategoryResponse> response;
        if(categoryResponse != null) {
            response = new ResponseEntity<>(categoryResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        boolean success = categoryService.deleteById(id);
        ResponseEntity<Void> response;
        if(success) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
