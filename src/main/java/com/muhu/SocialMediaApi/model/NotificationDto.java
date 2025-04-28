package com.muhu.SocialMediaApi.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class NotificationDto {
    private Long id;
    private String message;
    private Boolean isRead;
    private UserSummaryDto user;
}
