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
public class PostRegistrationDto {
    @NotBlank(message = "Please enter content.")
    private String content;
    private String imageUrl;
    @NotNull
    private User user;
}
