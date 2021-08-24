package org.xapps.services.productsservice.dtos;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "name cannot be empty")
    @Size(min = 3, message = "Name cannot be less than 3 characters")
    private String name;

    private String description;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
