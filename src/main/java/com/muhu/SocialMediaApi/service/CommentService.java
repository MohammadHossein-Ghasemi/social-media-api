package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment);
    Boolean deleteCommentById(Long commentId);
    Boolean deleteCommentByPostId(Long commentId);
    Boolean deleteCommentByUserEmail(String userEmail);
    Comment updateComment(Long postId,Comment comment);
    List<Comment> getAllComment();
    Comment getCommentById(Long commentId);
    List<Comment> getAllCommentByUserId(Long userId);
    List<Comment> getAllCommentByUserEmail(String userEmail);
    List<Comment> getAllCommentByPostId(Long postId);
}
