package com.muhu.SocialMediaApi.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class CommentDto {
    private Long id;
    private String content;
    private Long postId;
    private UserSummaryDto user;
}
