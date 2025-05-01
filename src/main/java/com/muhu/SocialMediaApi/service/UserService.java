package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {
    User saveUser(User user);
    Boolean deleteUserById(Long userId);
    Boolean deleteUserByEmail(String email);
    User updateUser(String email,User user);
    Page<UserDto> getAllUser(Integer page , Integer size);
    User getUserById(Long userId);
    User getUserByEmail(String email);
    Page<UserDto> getAllUserFollowersById(Long userId,Integer page , Integer size);
    Page<UserDto> getAllUserFollowingById(Long userId,Integer page , Integer size);
    Page<UserDto> getAllUserFollowersByEmail(String email, Integer page , Integer size);
    Page<UserDto> getAllUserFollowingByEmail(String email,Integer page , Integer size);
}
