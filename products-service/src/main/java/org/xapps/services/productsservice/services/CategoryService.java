package org.xapps.services.productsservice.services;

import org.xapps.services.productsservice.dtos.CategoryRequest;
import org.xapps.services.productsservice.dtos.CategoryResponse;

import java.util.List;


public interface CategoryService {

    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse edit(Long id, CategoryRequest request);

    boolean deleteById(Long id);

}
