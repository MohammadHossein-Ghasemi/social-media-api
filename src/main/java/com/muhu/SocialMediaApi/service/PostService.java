package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;
import org.springframework.data.domain.Page;

public interface PostService {
    PostDto savePost(PostRegistrationDto postRegistrationDto);
    Boolean deletePostById(Long postId);
    PostDto updatePostById(Long postId, Post post);
    PostDto getPostById(Long postId);
    Page<PostDto> getAllPost(Integer page,Integer size);
    Page<PostDto> getAllPostByUserId(Long userId,Integer page,Integer size);
    Page<PostDto> getAllPostByUserEmail(String userEmail,Integer page,Integer size);
}
