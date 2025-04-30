package com.muhu.SocialMediaApi.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LikeDto {
    private Long id;
    private UserSummaryDto user;
    private Long postId;
}
