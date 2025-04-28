package com.muhu.SocialMediaApi.model;

import com.muhu.SocialMediaApi.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class NotificationRegistrationDto {
    @NotBlank(message = "Please enter message.")
    private String message;
    private Boolean isRead;
    @NotNull
    private User user;
}
