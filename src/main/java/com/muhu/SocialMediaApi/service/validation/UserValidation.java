package com.muhu.SocialMediaApi.service.validation;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserRepository userRepository ;

    public boolean isUserValid(User user){
        if (user == null || user.getId() == null || user.getEmail() == null) {
            try {
                throw new BadRequestException("The use can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Long userId = user.getId();
        String userEmail = user.getEmail();

        if (!userRepository.existsByEmail(userEmail)) {
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        } else if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("There is no user with ID : " + userId);
        }

        return true;
    }
}
