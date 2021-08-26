package org.xapps.services.commentsservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "text")
    private String text;

    public Comment() {
    }

    public Comment(Long userId, Long productId, String text) {
        this.userId = userId;
        this.productId = productId;
        this.text = text;
    }
}
