package org.xapps.services.productsservice.services;

import org.xapps.services.productsservice.dtos.ProductResponse;

import java.util.List;


public interface ProductService {

    List<ProductResponse> getALl();

    ProductResponse getById(Long id);

}
