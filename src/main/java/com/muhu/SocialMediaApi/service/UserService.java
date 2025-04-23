package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Boolean deleteUserById(Long userId);
    Boolean deleteUserByEmail(String email);
    User updateUser(String email,User user);
    List<User> getAllUser();
    User getUserById(Long userId);
    User getUserByEmail(String email);
    List<User> getAllUserFollowersById(Long userId);
    List<User> getAllUserFollowingById(Long userId);
    List<User> getAllUserFollowersByEmail(String email);
    List<User> getAllUserFollowingByEmail(String email);
}
