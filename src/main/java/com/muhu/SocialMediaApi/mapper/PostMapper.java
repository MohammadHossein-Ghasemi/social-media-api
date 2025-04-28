package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;

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
        return PostDto.builder()
                .id(post.getId())
                .likes(post.getLikes())
                .comments(post.getComments())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .user(UserMapper.userToUserSummeryDto(post.getUser()))
                .build();
    }
}
