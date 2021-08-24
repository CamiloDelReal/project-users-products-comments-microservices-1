package org.xapps.services.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class CategoryRequest {
    @NotNull(message = "name cannot be empty")
    @Size(min = 3, message = "Name cannot be less than 3 characters")
    private String name;

    public CategoryRequest() {
    }

    public CategoryRequest(String name) {
        this.name = name;
    }
}
