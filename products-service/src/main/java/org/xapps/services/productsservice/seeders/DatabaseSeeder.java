package org.xapps.services.productsservice.seeders;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.xapps.services.productsservice.entities.Category;
import org.xapps.services.productsservice.entities.Product;
import org.xapps.services.productsservice.repositories.CategoryRepository;
import org.xapps.services.productsservice.repositories.ProductRepository;

import java.util.Collections;

@Component
public class DatabaseSeeder {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public DatabaseSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedCategories();
        seedProducts();
    }

    private void seedCategories() {
        if(categoryRepository.count() == 0) {
            Category beds = new Category("Beds");
            categoryRepository.save(beds);

            Category chairs = new Category("Chairs");
            categoryRepository.save(chairs);

            Category couchs = new Category("Couchs");
            categoryRepository.save(couchs);

            Category tables = new Category("Tables");
            categoryRepository.save(tables);

            Category lamps = new Category("Lamps");
            categoryRepository.save(lamps);
        }
    }

    private void seedProducts() {
        if(productRepository.count() == 0) {
            // Beds
            Category beds = categoryRepository.findByName("Beds");
            Product product = new Product("Bed_1", "Description_1", Collections.singletonList(beds));
            productRepository.save(product);
            product = new Product("Bed_1", "Description_1", Collections.singletonList(beds));
            productRepository.save(product);
            product = new Product("Bed_2", "Description_2", Collections.singletonList(beds));
            productRepository.save(product);
            product = new Product("Bed_3", "Description_3", Collections.singletonList(beds));
            productRepository.save(product);
            product = new Product("Bed_4", "Description_4", Collections.singletonList(beds));
            productRepository.save(product);
            product = new Product("Bed_5", "Description_5", Collections.singletonList(beds));
            productRepository.save(product);

            // Chairs
            Category chairs = categoryRepository.findByName("Chairs");
            product = new Product("Chair_6", "Description_6", Collections.singletonList(chairs));
            productRepository.save(product);
            product = new Product("Chair_7", "Description_7", Collections.singletonList(chairs));
            productRepository.save(product);
            product = new Product("Chair_8", "Description_8", Collections.singletonList(chairs));
            productRepository.save(product);
            product = new Product("Chair_9", "Description_9", Collections.singletonList(chairs));
            productRepository.save(product);
            product = new Product("Chair_10", "Description_10", Collections.singletonList(chairs));
            productRepository.save(product);

            // Couchs
            Category couchs = categoryRepository.findByName("Couchs");
            product = new Product("Couch_11", "Description_11", Collections.singletonList(couchs));
            productRepository.save(product);
            product = new Product("Couch_12", "Description_12", Collections.singletonList(couchs));
            productRepository.save(product);
            product = new Product("Couch_13", "Description_13", Collections.singletonList(couchs));
            productRepository.save(product);
            product = new Product("Couch_14", "Description_14", Collections.singletonList(couchs));
            productRepository.save(product);
            product = new Product("Couch_15", "Description_15", Collections.singletonList(couchs));
            productRepository.save(product);

            // Tables
            Category tables = categoryRepository.findByName("Tables");
            product = new Product("Table_16", "Description_16", Collections.singletonList(tables));
            productRepository.save(product);
            product = new Product("Table_17", "Description_17", Collections.singletonList(tables));
            productRepository.save(product);
            product = new Product("Table_18", "Description_18", Collections.singletonList(tables));
            productRepository.save(product);
            product = new Product("Table_19", "Description_19", Collections.singletonList(tables));
            productRepository.save(product);
            product = new Product("Table_20", "Description_20", Collections.singletonList(tables));
            productRepository.save(product);

            // Lamps
            Category lamps = categoryRepository.findByName("Lamps");
            product = new Product("Lamp_21", "Description_21", Collections.singletonList(lamps));
            productRepository.save(product);
            product = new Product("Lamp_22", "Description_22", Collections.singletonList(lamps));
            productRepository.save(product);
            product = new Product("Lamp_23", "Description_23", Collections.singletonList(lamps));
            productRepository.save(product);
            product = new Product("Lamp_24", "Description_24", Collections.singletonList(lamps));
            productRepository.save(product);
            product = new Product("Lamp_25", "Description_25", Collections.singletonList(lamps));
            productRepository.save(product);
        }
    }
}
