package org.xapps.services.productsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.services.productsservice.dtos.ProductResponse;
import org.xapps.services.productsservice.entities.Product;
import org.xapps.services.productsservice.repositories.ProductRepository;
import org.xapps.services.productsservice.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private ModelMapper modelMapper;
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
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
        }
        return response;
    }
}
