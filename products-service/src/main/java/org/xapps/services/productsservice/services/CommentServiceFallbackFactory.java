package org.xapps.services.productsservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class CommentServiceFallbackFactory implements FallbackFactory<CommentService> {

    private final Logger logger = LoggerFactory.getLogger(CommentServiceFallbackFactory.class);

    @Override
    public CommentService create(Throwable cause) {
        logger.info("CommentService unavailable, creating fallback service");
        return new CommentServiceFallback();
    }

}
