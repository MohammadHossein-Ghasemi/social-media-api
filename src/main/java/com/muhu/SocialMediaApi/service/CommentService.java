package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.model.CommentDto;
import com.muhu.SocialMediaApi.model.CommentRegistrationDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentDto saveComment(CommentRegistrationDto commentRegistrationDto);
    Boolean deleteCommentById(Long commentId);
    Boolean deleteCommentByPostId(Long commentId);
    Boolean deleteCommentByUserEmail(String userEmail);
    CommentDto updateComment(Long commentId,Comment comment);
    Page<CommentDto> getAllComment(Integer page , Integer size);
    CommentDto getCommentById(Long commentId);
    Page<CommentDto> getAllCommentByUserId(Long userId,Integer page , Integer size);
    Page<CommentDto> getAllCommentByUserEmail(String userEmail,Integer page , Integer size);
    Page<CommentDto> getAllCommentByPostId(Long postId,Integer page , Integer size);
}
