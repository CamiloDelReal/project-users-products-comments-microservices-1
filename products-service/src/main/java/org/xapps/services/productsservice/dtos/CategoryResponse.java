package org.xapps.services.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.xapps.services.productsservice.entities.Product;

import java.util.List;


// This dto entity is not really necessary for a project like this
@Setter
@Getter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<Product> products;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public CategoryResponse(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public CategoryResponse(String name) {
        this.name = name;
    }
}
