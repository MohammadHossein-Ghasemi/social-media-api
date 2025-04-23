package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface PostService {
    Post savePost(Post post) throws BadRequestException;
    Boolean deletePostById(Long postId);
    Post updatePostById(Long postId, Post post);
    Post getPostById(Long postId);
    List<Post> getAllPost();
    List<Post> getAllPostByUserId(Long userId);
    List<Post> getAllPostByUserEmail(String userEmail);
}
