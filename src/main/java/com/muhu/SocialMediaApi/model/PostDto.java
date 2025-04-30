package com.muhu.SocialMediaApi.model;

import com.muhu.SocialMediaApi.entity.Comment;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class PostDto {
    private Long id;
    private String content;
    private String imageUrl;
    private UserSummaryDto user;
    private Set<Comment> comments;
    private Set<Long> likeId;
}
