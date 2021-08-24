package org.xapps.services.productsservice.services;

import org.xapps.services.productsservice.dtos.CategoryResponse;

import java.util.List;


public interface CategoryService {

    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id);

}
