package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    Boolean deletePostById(Long postId);
    Post updatePostById(Long postId, Post post);
    Post getPostById(Long postId);
    List<Post> getAllPost();
    List<Post> getAllPostByUserId(Long userId);
    List<Post> getAllPostByUserEmail(String userEmail);
}
