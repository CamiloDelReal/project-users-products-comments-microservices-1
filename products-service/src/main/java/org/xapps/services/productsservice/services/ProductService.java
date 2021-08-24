package org.xapps.services.productsservice.services;

import org.xapps.services.productsservice.dtos.ProductRequest;
import org.xapps.services.productsservice.dtos.ProductResponse;

import java.util.List;


public interface ProductService {

    List<ProductResponse> getALl();

    ProductResponse getById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse edit(Long id, ProductRequest request);

    boolean deleteById(Long id);

}
