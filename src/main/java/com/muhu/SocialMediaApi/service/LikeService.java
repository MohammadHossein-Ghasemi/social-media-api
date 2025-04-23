package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Like;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface LikeService {
    Like saveLike(Like like) throws BadRequestException;
    Boolean deleteLikeById(Long likeId);
    Like updateLike(Long likeId,Like like);
    Like getLikeByID(Long likeId);
    List<Like> getAllLike();
    List<Like> getAllLikeByUserId(Long userId);
    List<Like> getAllLikeByUserEmail(String userEmail);
    List<Like> getAllLikeByPostId(Long postId);

}
