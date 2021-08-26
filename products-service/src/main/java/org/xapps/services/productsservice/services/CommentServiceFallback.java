package org.xapps.services.productsservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xapps.services.productsservice.dtos.CommentResponse;

import java.util.ArrayList;
import java.util.List;


public class CommentServiceFallback implements CommentService{

    private final Logger logger = LoggerFactory.getLogger(CommentServiceFallback.class);
    @Override
    public List<CommentResponse> getCommentByProductId(Long productId) {
        logger.info("CommentServiceFallback returning an empty list");
        return new ArrayList<>();
    }

}
