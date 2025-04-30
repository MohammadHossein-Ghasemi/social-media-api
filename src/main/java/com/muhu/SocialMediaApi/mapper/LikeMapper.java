package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;

public class LikeMapper {
    public static Like likeRegistrationDtoToLike(LikeRegistrationDto likeRegistrationDto){
        return Like.builder()
                .user(likeRegistrationDto.getUser())
                .post(likeRegistrationDto.getPost())
                .build();
    }

    public static LikeDto likeToLikeDto(Like like){
        return LikeDto.builder()
                .id(like.getId())
                .postId(like.getPost().getId())
                .user(UserMapper.userToUserSummeryDto(like.getUser()))
                .build();
    }
}
