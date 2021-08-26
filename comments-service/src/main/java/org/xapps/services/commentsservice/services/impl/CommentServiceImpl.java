package org.xapps.services.commentsservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xapps.services.commentsservice.dtos.CommentCreateRequest;
import org.xapps.services.commentsservice.dtos.CommentEditRequest;
import org.xapps.services.commentsservice.dtos.CommentResponse;
import org.xapps.services.commentsservice.entities.Comment;
import org.xapps.services.commentsservice.repositories.CommentRepository;
import org.xapps.services.commentsservice.services.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CommentResponse> getAllByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId).orElse(null);
        List<CommentResponse> response;
        if(comments != null) {
            response = comments.stream().map(it -> modelMapper.map(it, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public List<CommentResponse> getAllByProductId(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId).orElse(null);
        List<CommentResponse> response;
        if(comments != null) {
            response = comments.stream().map(it -> modelMapper.map(it, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public List<CommentResponse> getAllByUserIdAndProductId(Long userId, Long productId) {
        List<Comment> comments = commentRepository.findByUserIdAndProductId(userId, productId).orElse(null);
        List<CommentResponse> response;
        if(comments != null) {
            response = comments.stream().map(it -> modelMapper.map(it, CommentResponse.class)).collect(Collectors.toList());
        } else {
            response = new ArrayList<>();
        }
        return response;
    }

    @Override
    public CommentResponse create(CommentCreateRequest commentRequest) {
        Comment comment = modelMapper.map(commentRequest, Comment.class);
        CommentResponse response = null;
        try {
            commentRepository.save(comment);
            response = modelMapper.map(comment, CommentResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public CommentResponse edit(Long id, CommentEditRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElse(null);
        CommentResponse response = null;
        if(comment != null) {
            comment.setText(commentRequest.getText());
            commentRepository.save(comment);
            response = modelMapper.map(comment, CommentResponse.class);
        }
        return response;
    }

    @Override
    public boolean delete(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        boolean success = false;
        if(comment != null) {
            commentRepository.deleteById(id);
            success = true;
        }
        return success;
    }

    @Override
    public boolean isOwner(Long id, Long userId) {
        Comment comment = commentRepository.findByIdAndUserId(id, userId).orElse(null);
        return comment != null;
    }
}
