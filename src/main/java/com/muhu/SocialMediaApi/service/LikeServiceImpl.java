package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.LikeRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    public Like saveLike(Like like){

        if (!userValidation(like.getUser()) || !postValidation(like.getPost())){
            return null;
        }

        return likeRepository.save(like);
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
    public Like updateLike(Long likeId, Like like) {
        AtomicReference<Like> updatedLike = new AtomicReference<>();

        likeRepository.findById(likeId).ifPresentOrElse(
                foundedLike->{
                    updateNonNullFields(foundedLike,like);
                    updatedLike.set(likeRepository.save(like));
                },
                ()->{throw new ResourceNotFoundException("There is no like with ID : "+likeId);}
        );
        return updatedLike.get();
    }

    @Override
    public Like getLikeByID(Long likeId) {
        return likeRepository.findById(likeId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no like with ID : "+likeId));
    }

    @Override
    public List<Like> getAllLike() {
        return likeRepository.findAll();
    }

    @Override
    public List<Like> getAllLikeByUserId(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return likeRepository.findByUserId(userId);
    }

    @Override
    public List<Like> getAllLikeByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        return likeRepository.findByUserEmail(userEmail);
    }

    @Override
    public List<Like> getAllLikeByPostId(Long postId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        return likeRepository.findByPostId(postId);
    }

    private boolean userValidation(User user)  {
        if (user == null || user.getId() == null || user.getEmail() == null) {
            try {
                throw new BadRequestException("The use can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Long userId = user.getId();
        String userEmail = user.getEmail();

        if (!userRepository.existsByEmail(userEmail)) {
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        } else if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("There is no user with ID : " + userId);
        }

        return true;
    }
    private boolean postValidation(Post post){
        if (post == null || post.getId() == null ){
            try {
                throw new BadRequestException("The post can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Long postId = post.getId();

        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }

        return true;
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
