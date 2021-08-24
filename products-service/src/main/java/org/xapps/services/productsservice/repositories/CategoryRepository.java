package org.xapps.services.productsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xapps.services.productsservice.entities.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

}
