package com.muhu.SocialMediaApi.service.validation;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validation {

    private final UserRepository userRepository ;
    private final PostRepository postRepository;

    public User isUserValid(User user){
        if (user == null || user.getId() == null || user.getEmail() == null) {
            try {
                throw new BadRequestException("The use can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        return userRepository.findByEmail(user.getEmail()).orElseThrow(
                ()-> new ResourceNotFoundException("There is no user with this profile."));
    }

    public Post isPostValid(Post post){
        if (post == null || post.getId() == null ){
            try {
                throw new BadRequestException("The post can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        return postRepository.findById(post.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("There is no post with this profile."));
    }

    public Pageable pageAndSizeValidation(Integer page , Integer size){
        if (page == null || page<0){
            page=0;
        }
        if (size == null || size<=0){
            size=5;
        }
        return PageRequest.of(page,size);
    }
}
