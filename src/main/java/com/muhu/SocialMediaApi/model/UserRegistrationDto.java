package com.muhu.SocialMediaApi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserRegistrationDto{
    @NotBlank(message = "Please enter a username.")
    private String username;

    @Email(message = "Please enter valid email.")
    @NotBlank(message = "Please enter an email.")
    private String email;

    @NotBlank(message = "Please enter a password.")
    private String password;

    private String bio;

    private String profilePictureUrl;
}
