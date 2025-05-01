package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.mapper.LikeMapper;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;
import com.muhu.SocialMediaApi.repository.LikeRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

import static com.muhu.SocialMediaApi.mapper.LikeMapper.likeRegistrationDtoToLike;
import static com.muhu.SocialMediaApi.mapper.LikeMapper.likeToLikeDto;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final Validation validation;
    @Override
    public LikeDto saveLike(LikeRegistrationDto likeRegistrationDto){
        User userValid = validation.isUserValid(likeRegistrationDto.getUser());
        Post postValid = validation.isPostValid(likeRegistrationDto.getPost());
        if (null == userValid || null == postValid){
            return null;
        }

        likeRegistrationDto.setUser(userValid);
        likeRegistrationDto.setPost(postValid);

        return likeToLikeDto(likeRepository.save(likeRegistrationDtoToLike(likeRegistrationDto)));
    }

    @Override
    public Boolean deleteLikeById(Long likeId) {
        if (!likeRepository.existsById(likeId)){
            throw new ResourceNotFoundException("There is no like with ID : "+likeId);
        }
        likeRepository.deleteById(likeId);
        return true;
    }

    @Override
    public LikeDto updateLike(Long likeId, Like like) {
        AtomicReference<Like> updatedLike = new AtomicReference<>();

        likeRepository.findById(likeId).ifPresentOrElse(
                foundedLike->{
                    updateNonNullFields(foundedLike,like);
                    updatedLike.set(likeRepository.save(foundedLike));
                },
                ()->{throw new ResourceNotFoundException("There is no like with ID : "+likeId);}
        );
        return likeToLikeDto(updatedLike.get());
    }

    @Override
    public LikeDto getLikeByID(Long likeId) {
        return likeToLikeDto(likeRepository.findById(likeId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no like with ID : "+likeId)));
    }

    @Override
    public Page<LikeDto> getAllLike(Integer page , Integer size) {
        return likeRepository.findAll(validation.pageAndSizeValidation(page,size))
                .map(LikeMapper::likeToLikeDto);
    }

    @Override
    public Page<LikeDto> getAllLikeByUserId(Long userId , Integer page , Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return likeRepository.findByUserId(userId,validation.pageAndSizeValidation(page,size))
                .map(LikeMapper::likeToLikeDto);
    }

    @Override
    public Page<LikeDto> getAllLikeByUserEmail(String userEmail , Integer page , Integer size) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        return likeRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(page, size))
                .map(LikeMapper::likeToLikeDto);
    }

    @Override
    public Page<LikeDto> getAllLikeByPostId(Long postId , Integer page , Integer size) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        return likeRepository.findByPostId(postId,validation.pageAndSizeValidation(page, size))
                .map(LikeMapper::likeToLikeDto);
    }

    private void updateNonNullFields(Like updatedLike , Like inputLike){
        if (inputLike.getId() != null) updatedLike.setId(inputLike.getId());

        if (inputLike.getUser() != null) {
            Long userId = inputLike.getUser().getId();
            String userEmail = inputLike.getUser().getEmail();

            if (!userRepository.existsByEmail(userEmail)) {
                throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
            } else if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException("There is no user with ID : " + userId);
            }

            updatedLike.setUser(inputLike.getUser());
        }


        if (inputLike.getPost() != null) {

            Long postId = inputLike.getPost().getId();

            if (!postRepository.existsById(postId)) {
                throw new ResourceNotFoundException("There is no post with ID : "+postId);
            }

            updatedLike.setPost(inputLike.getPost());
        }
    }
}
