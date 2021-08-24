package org.xapps.services.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.xapps.services.productsservice.entities.Category;

import java.util.List;


// This dto entity is not really necessary for a project like this
@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private List<Category> categories;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
    }
}
