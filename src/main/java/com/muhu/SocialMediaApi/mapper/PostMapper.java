package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;

import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {
    private PostMapper () {}

    public static Post postRegistrationDtoToPost(PostRegistrationDto postRegistrationDto){
        return Post.builder()
                .content(postRegistrationDto.getContent())
                .user(postRegistrationDto.getUser())
                .imageUrl(postRegistrationDto.getImageUrl())
                .build();
    }

    public static PostDto postToPostDto(Post post){
        Set<Long> likes;

        if (null != post.getLikes()){
            likes= post.getLikes().stream()
                    .map(Like::getId)
                    .collect(Collectors.toSet());
        }else {
            likes = Set.of();
        }

        return PostDto.builder()
                .id(post.getId())
                .likeId(likes)
                .comments(post.getComments())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .user(UserMapper.userToUserSummeryDto(post.getUser()))
                .build();
    }
}
