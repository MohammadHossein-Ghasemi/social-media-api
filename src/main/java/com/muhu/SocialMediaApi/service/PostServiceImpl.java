package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Validation Validation;

    @Override
    public Post savePost(Post post) {
        if (!Validation.isUserValid(post.getUser())){
            return null;
        }
        return postRepository.save(post);
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
    public Post updatePostById(Long postId, Post post) {
        AtomicReference<Post> updatedPost = new AtomicReference<>();

        postRepository.findById(postId).ifPresentOrElse(
                foundedPost->{
                    updateNonNullFields(foundedPost,post);
                    updatedPost.set(postRepository.save(foundedPost));
                },()->{throw new ResourceNotFoundException("There is no post with ID : "+postId);}
        );
        return updatedPost.get();
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("There is no post with ID : "+postId));
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllPostByUserId(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return postRepository.findByUserId(userId);
    }

    @Override
    public List<Post> getAllPostByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        }
        return postRepository.findByUserEmail(userEmail);
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
