package org.xapps.services.commentsservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xapps.services.commentsservice.entities.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findByUserId(Long userId);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);

    Optional<List<Comment>> findByProductId(Long productId);

    Optional<List<Comment>> findByUserIdAndProductId(Long userId, Long productId);

}
