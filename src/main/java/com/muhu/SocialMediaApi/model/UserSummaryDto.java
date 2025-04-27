package com.muhu.SocialMediaApi.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserSummaryDto {
    private Long userId;
    private String username;
    private String email;
    private String profilePictureUrl;
}
