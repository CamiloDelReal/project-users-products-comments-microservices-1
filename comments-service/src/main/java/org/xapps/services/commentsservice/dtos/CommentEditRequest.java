package org.xapps.services.commentsservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class CommentEditRequest {
    @NotNull(message = "Comment text cannot be empty")
    private String text;

    public CommentEditRequest() {
    }

    public CommentEditRequest(String text) {
        this.text = text;
    }
}
