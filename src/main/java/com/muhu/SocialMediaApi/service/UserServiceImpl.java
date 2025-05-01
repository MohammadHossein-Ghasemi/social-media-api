package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.DuplicateException;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.mapper.UserMapper;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new DuplicateException();
        }
        return userRepository.save(user);
    }

    @Override
    public Boolean deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : " + userId);
        }
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public Boolean deleteUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("There is no user with Email : " + email);
        }
        userRepository.deleteByEmail(email);
        return true;
    }

    @Override
    public User updateUser(String email, User user) {
        AtomicReference<User> updatedUser= new AtomicReference<>();

          userRepository.findByEmail(email).ifPresentOrElse(
                foundedUser->{
                    updateNonNullFields(foundedUser,user);
                    updatedUser.set(userRepository.save(foundedUser));
                },
                ()-> {throw new ResourceNotFoundException("There is no user with Email : " + email+
                        " and ID : "+user.getId());}
        );
        return updatedUser.get();
    }

    @Override
    public Page<UserDto> getAllUser(Integer page , Integer size) {
        return userRepository.findAll(validation.pageAndSizeValidation(page,size))
                .map(UserMapper::userToUserDto);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("There is no user with ID : "+userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("There is no user with Email : " + email));
    }

    @Override
    public Page<UserDto> getAllUserFollowersById(Long userId,Integer page , Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return userRepository.findAllFollowersById(userId,validation.pageAndSizeValidation(page,size))
                .map(UserMapper::userToUserDto);
    }

    @Override
    public Page<UserDto> getAllUserFollowingById(Long userId,Integer page , Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return userRepository.findAllFollowingById(userId,validation.pageAndSizeValidation(page,size))
                .map(UserMapper::userToUserDto);
    }

    @Override
    public Page<UserDto> getAllUserFollowersByEmail(String email,Integer page , Integer size) {
        if (!userRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("There is no user with Email : " + email);
        }
        return userRepository.findAllFollowersByEmail(email,validation.pageAndSizeValidation(page,size))
                .map(UserMapper::userToUserDto);
    }

    @Override
    public Page<UserDto> getAllUserFollowingByEmail(String email,Integer page , Integer size) {
        if (!userRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("There is no user with Email : " + email);
        }
        return userRepository.findAllFollowingByEmail(email,validation.pageAndSizeValidation(page,size))
                .map(UserMapper::userToUserDto);
    }

    private void updateNonNullFields(User updatedUser , User inputUser){
        if(inputUser.getId() != null)updatedUser.setId(inputUser.getId());
        if(inputUser.getEmail() != null)updatedUser.setEmail(inputUser.getEmail());
        if(inputUser.getUsername() != null)updatedUser.setUsername(inputUser.getUsername());
        if(inputUser.getBio() != null)updatedUser.setBio(inputUser.getBio());
        if(inputUser.getProfilePictureUrl() != null)updatedUser.setProfilePictureUrl(inputUser.getProfilePictureUrl());
        if(inputUser.getPosts() != null)updatedUser.setPosts(inputUser.getPosts());
        if(inputUser.getFollowers() != null)updatedUser.setFollowers(inputUser.getFollowers());
        if(inputUser.getFollowing() != null)updatedUser.setFollowing(inputUser.getFollowing());
        if(inputUser.getLikes() != null)updatedUser.setLikes(inputUser.getLikes());
        if(inputUser.getNotifications() != null)updatedUser.setNotifications(inputUser.getNotifications());
        if(inputUser.getComments() != null)updatedUser.setComments(inputUser.getComments());
    }
}
