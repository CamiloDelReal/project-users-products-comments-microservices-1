package org.xapps.services.commentsservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class CommentCreateRequest {
    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @NotNull(message = "Product id cannot be empty")
    private Long productId;

    @NotNull(message = "Comment text cannot be empty")
    private String text;

    public CommentCreateRequest() {
    }

    public CommentCreateRequest(Long userId, Long productId, String text) {
        this.userId = userId;
        this.productId = productId;
        this.text = text;
    }
}
