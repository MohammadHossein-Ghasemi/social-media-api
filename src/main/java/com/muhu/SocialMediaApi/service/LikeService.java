package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;
import org.springframework.data.domain.Page;

public interface LikeService {
    LikeDto saveLike(LikeRegistrationDto likeRegistrationDto);
    Boolean deleteLikeById(Long likeId);
    LikeDto updateLike(Long likeId,Like like);
    LikeDto getLikeByID(Long likeId);
    Page<LikeDto> getAllLike(Integer page , Integer size);
    Page<LikeDto> getAllLikeByUserId(Long userId,Integer page ,Integer size);
    Page<LikeDto> getAllLikeByUserEmail(String userEmail,Integer page ,Integer size);
    Page<LikeDto> getAllLikeByPostId(Long postId,Integer page ,Integer size);

}
