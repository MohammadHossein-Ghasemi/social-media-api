package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.model.UserRegistrationDto;
import com.muhu.SocialMediaApi.model.UserSummaryDto;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper(){}

    public static User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        return User.builder()
                .username(userRegistrationDto.getUsername())
                .email(userRegistrationDto.getEmail())
                .password(userRegistrationDto.getPassword())
                .bio(userRegistrationDto.getBio())
                .profilePictureUrl(userRegistrationDto.getProfilePictureUrl())
                .build();
    }

    public static UserDto userToUserDto(User user) {
        Set<UserSummaryDto> followers ;
        Set<UserSummaryDto> following ;
        Set<PostDto> postDtos;

        if (null != user.getPosts()){
            postDtos = user.getPosts().stream()
                    .map(PostMapper::postToPostDto)
                    .collect(Collectors.toSet());
        }else {
            postDtos = Set.of();
        }

        if (null != user.getFollowers()){
            followers = user.getFollowers().stream()
                    .map(UserMapper::userToUserSummeryDto)
                    .collect(Collectors.toSet());

        } else {
            followers = Set.of();
        }

        if (null != user.getFollowing()) {
            following = user.getFollowers().stream()
                    .map(UserMapper::userToUserSummeryDto)
                    .collect(Collectors.toSet());
        } else {
            following = Set.of();
        }

        return UserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .bio(user.getBio())
                .profilePictureUrl(user.getProfilePictureUrl())
                .posts(postDtos)
                .followers(followers)
                .following(following)
                .likes(user.getLikes())
                .notifications(user.getNotifications())
                .comments(user.getComments())
                .build();
    }

    public static UserSummaryDto userToUserSummeryDto(User user){
        return UserSummaryDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}
