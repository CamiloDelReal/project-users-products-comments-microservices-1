package org.xapps.services.productsservice.controllers;

import brave.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xapps.services.productsservice.dtos.ProductRequest;
import org.xapps.services.productsservice.dtos.ProductResponse;
import org.xapps.services.productsservice.services.ProductService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> productResponse = productService.getALl();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> getById(@PathVariable("id") Long id) {
        ProductResponse productResponse = productService.getById(id);
        ResponseEntity<ProductResponse> response;
        if(productResponse != null) {
            response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest);
        ResponseEntity<ProductResponse> response;
        if(productResponse != null) {
            response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        } else {
            // Sql error (name duplicity for example)
            response = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return response;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<ProductResponse> editProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.edit(id, productRequest);
        ResponseEntity<ProductResponse> response;
        if(productResponse != null) {
            response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated() and hasAuthority('Administrator')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        boolean success = productService.deleteById(id);
        ResponseEntity<Void> response;
        if(success) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

}
