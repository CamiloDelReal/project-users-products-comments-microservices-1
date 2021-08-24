package org.xapps.services.productsservice.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "categories_products")
@Getter
@Setter
public class CategoryProduct {

    @EmbeddedId
    private CategoryProductId id;

    @Data
    @Embeddable
    public static class CategoryProductId implements Serializable {
        @Column(name = "category_id")
        private Long categoryId;

        @Column(name = "product_id")
        private Long productId;
    }
}
