package org.xapps.services.productsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xapps.services.productsservice.dtos.ProductResponse;
import org.xapps.services.productsservice.services.ProductService;

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

}
