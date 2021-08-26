package org.xapps.services.productsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.services.productsservice.dtos.CategoryResponse;
import org.xapps.services.productsservice.dtos.CommentResponse;
import org.xapps.services.productsservice.dtos.ProductRequest;
import org.xapps.services.productsservice.dtos.ProductResponse;
import org.xapps.services.productsservice.entities.Category;
import org.xapps.services.productsservice.entities.Product;
import org.xapps.services.productsservice.repositories.ProductRepository;
import org.xapps.services.productsservice.services.CommentService;
import org.xapps.services.productsservice.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private ModelMapper modelMapper;
    private ProductRepository productRepository;
    private CommentService commentService;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository, CommentService commentService) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.commentService = commentService;
    }

    @Override
    public List<ProductResponse> getALl() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response;
        if(!products.isEmpty()) {
            response = products.stream().map(it -> modelMapper.map(it, ProductResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductResponse response = null;
        if(product != null) {
            response = modelMapper.map(product, ProductResponse.class);
            List<CommentResponse> comments = commentService.getCommentByProductId(id);
            response.setComments(comments);
        }
        return response;
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = modelMapper.map(request, Product.class);
        ProductResponse response = null;
        try {
            productRepository.save(product);
            response = modelMapper.map(product, ProductResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public ProductResponse edit(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElse(null);
        ProductResponse response = null;
        if(product != null) {
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            productRepository.save(product);
            response = modelMapper.map(product, ProductResponse.class);
        }
        return response;
    }

    @Override
    public boolean deleteById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        boolean success = false;
        if(product != null) {
            productRepository.deleteById(id);
            success = true;
        }
        return success;
    }
}
