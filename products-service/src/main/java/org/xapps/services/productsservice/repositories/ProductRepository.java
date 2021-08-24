package org.xapps.services.productsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xapps.services.productsservice.entities.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
