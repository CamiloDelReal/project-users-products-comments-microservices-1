package org.xapps.services.productsservice.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String text;

    public CommentResponse() {
    }

    public CommentResponse(Long id, Long userId, String text) {
        this.id = id;
        this.userId = userId;
        this.text = text;
    }
}
