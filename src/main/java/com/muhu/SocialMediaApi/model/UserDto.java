package com.muhu.SocialMediaApi.model;

import com.muhu.SocialMediaApi.entity.Authority;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String bio;
    private String profilePictureUrl;
    private Set<PostDto> posts;
    private Set<UserSummaryDto> followers;
    private Set<UserSummaryDto> following;
    private Set<Long> likeId;
    private Set<NotificationDto> notifications;
    private Set<Long> commentId;
    private Set<Authority> authorities;
}
