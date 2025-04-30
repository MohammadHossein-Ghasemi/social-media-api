package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.model.CommentDto;
import com.muhu.SocialMediaApi.model.CommentRegistrationDto;

public class CommentMapper {
    public static Comment commentRegistrationDtoToComment(CommentRegistrationDto commentRegistrationDto){
        return Comment.builder()
                .user(commentRegistrationDto.getUser())
                .post(commentRegistrationDto.getPost())
                .content(commentRegistrationDto.getContent())
                .build();
    }

    public static CommentDto commentToCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .user(UserMapper.userToUserSummeryDto(comment.getUser()))
                .build();
    }
}
