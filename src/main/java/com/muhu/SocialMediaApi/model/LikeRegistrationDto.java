package com.muhu.SocialMediaApi.model;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LikeRegistrationDto {
    @NotNull
    private User user;

    @NotNull
    private Post post;
}
