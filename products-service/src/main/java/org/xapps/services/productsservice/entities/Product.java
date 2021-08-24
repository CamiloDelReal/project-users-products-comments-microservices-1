package org.xapps.services.productsservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "products")
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Category> categories;

    public Product() {
    }

    public Product(String name, String description, List<Category> categories) {
        this.name = name;
        this.description = description;
        this.categories = categories;
    }
}
