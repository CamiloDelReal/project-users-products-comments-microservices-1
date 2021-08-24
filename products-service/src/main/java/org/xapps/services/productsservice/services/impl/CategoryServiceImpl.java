package org.xapps.services.productsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.services.productsservice.dtos.CategoryRequest;
import org.xapps.services.productsservice.dtos.CategoryResponse;
import org.xapps.services.productsservice.entities.Category;
import org.xapps.services.productsservice.repositories.CategoryRepository;
import org.xapps.services.productsservice.services.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> response;
        if(!categories.isEmpty()) {
            response = categories.stream().map(it -> modelMapper.map(it, CategoryResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        CategoryResponse response = null;
        if (category != null) {
            response = modelMapper.map(category, CategoryResponse.class);
        }
        return response;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = modelMapper.map(request, Category.class);
        CategoryResponse response = null;
        try {
            categoryRepository.save(category);
            response = modelMapper.map(category, CategoryResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public CategoryResponse edit(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElse(null);
        CategoryResponse response = null;
        if(category != null) {
            category.setName(request.getName());
            categoryRepository.save(category);
            response = modelMapper.map(category, CategoryResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteById(Long id) {
        categoryRepository.deleteById(id);
        return true;
    }
}
