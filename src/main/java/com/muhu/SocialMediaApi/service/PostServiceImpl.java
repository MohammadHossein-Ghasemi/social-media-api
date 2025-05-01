package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.mapper.PostMapper;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

import static com.muhu.SocialMediaApi.mapper.PostMapper.postRegistrationDtoToPost;
import static com.muhu.SocialMediaApi.mapper.PostMapper.postToPostDto;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public PostDto savePost(PostRegistrationDto postRegistrationDto) {
        User userValid = validation.isUserValid(postRegistrationDto.getUser());
        if (null == userValid){
            return null;
        }
        postRegistrationDto.setUser(userValid);
        return postToPostDto(postRepository.save(postRegistrationDtoToPost(postRegistrationDto)));
    }

    @Override
    public Boolean deletePostById(Long postId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        postRepository.deleteById(postId);
        return true;
    }

    @Override
    public PostDto updatePostById(Long postId, Post post) {
        AtomicReference<Post> updatedPost = new AtomicReference<>();

        postRepository.findById(postId).ifPresentOrElse(
                foundedPost->{
                    updateNonNullFields(foundedPost,post);
                    updatedPost.set(postRepository.save(foundedPost));
                },()->{throw new ResourceNotFoundException("There is no post with ID : "+postId);}
        );
        return postToPostDto(updatedPost.get());
    }

    @Override
    public PostDto getPostById(Long postId) {
        return postToPostDto(postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("There is no post with ID : "+postId)));
    }

    @Override
    public Page<PostDto> getAllPost(Integer page,Integer size) {

        return postRepository.findAll(validation.pageAndSizeValidation(page,size))
                .map(PostMapper::postToPostDto);
    }

    @Override
    public Page<PostDto> getAllPostByUserId(Long userId,Integer page,Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return postRepository.findByUserId(userId,validation.pageAndSizeValidation(page,size))
                .map(PostMapper::postToPostDto);
    }

    @Override
    public Page<PostDto> getAllPostByUserEmail(String userEmail,Integer page,Integer size) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        }
        return postRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(page,size))
                .map(PostMapper::postToPostDto);
    }

    private void updateNonNullFields(Post updatedPost , Post inputPost){
        if (inputPost.getId() != null) updatedPost.setId(inputPost.getId());
        if (inputPost.getLikes() != null) updatedPost.setLikes(inputPost.getLikes());
        if (inputPost.getComments() != null) updatedPost.setComments(inputPost.getComments());
        if (inputPost.getContent() != null) updatedPost.setContent(inputPost.getContent());
        if (inputPost.getUser() != null){
            Long userId = inputPost.getUser().getId();
            String userEmail = inputPost.getUser().getEmail();

            if (!userRepository.existsByEmail(userEmail)) {
                throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
            } else if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException("There is no user with ID : " + userId);
            }

            updatedPost.setUser(inputPost.getUser());
        }
        if (inputPost.getImageUrl() != null) updatedPost.setImageUrl(inputPost.getImageUrl());
    }
}
