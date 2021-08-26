package org.xapps.services.commentsservice.services;

import org.xapps.services.commentsservice.dtos.CommentCreateRequest;
import org.xapps.services.commentsservice.dtos.CommentEditRequest;
import org.xapps.services.commentsservice.dtos.CommentResponse;

import java.util.List;


public interface CommentService {

    List<CommentResponse> getAllByUserId(Long userId);

    List<CommentResponse> getAllByProductId(Long productId);

    List<CommentResponse> getAllByUserIdAndProductId(Long userId, Long productId);

    CommentResponse create(CommentCreateRequest commentRequest);

    CommentResponse edit(Long id, CommentEditRequest commentRequest);

    boolean delete(Long id);

    boolean isOwner(Long commentId, Long userId);
}
