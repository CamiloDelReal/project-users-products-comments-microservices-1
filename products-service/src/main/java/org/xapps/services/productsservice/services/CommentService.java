package org.xapps.services.productsservice.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xapps.services.productsservice.dtos.CommentResponse;

import java.util.List;


@FeignClient(name = "${remote.services.comments}", fallbackFactory = CommentServiceFallbackFactory.class)
public interface CommentService {

    @GetMapping(path = "/products/{productId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentResponse> getCommentByProductId(@PathVariable("productId") Long productId);

}
