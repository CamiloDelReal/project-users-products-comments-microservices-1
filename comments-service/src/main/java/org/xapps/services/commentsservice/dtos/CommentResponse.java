package org.xapps.services.commentsservice.dtos;

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

    public CommentResponse(Long id, Long userId, Long productId, String text) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.text = text;
    }
}
