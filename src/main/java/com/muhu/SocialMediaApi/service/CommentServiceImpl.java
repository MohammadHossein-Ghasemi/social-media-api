package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.mapper.CommentMapper;
import com.muhu.SocialMediaApi.model.CommentDto;
import com.muhu.SocialMediaApi.model.CommentRegistrationDto;
import com.muhu.SocialMediaApi.repository.CommentRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;
import static com.muhu.SocialMediaApi.mapper.CommentMapper.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final Validation validation;

    @Override
    public CommentDto saveComment(CommentRegistrationDto commentRegistrationDto) {
        User userValid = validation.isUserValid(commentRegistrationDto.getUser());
        Post postValid = validation.isPostValid(commentRegistrationDto.getPost());
        if (null == userValid || null == postValid ) {
            return null;
        }

        commentRegistrationDto.setUser(userValid);
        commentRegistrationDto.setPost(postValid);
        return commentToCommentDto(commentRepository.save(commentRegistrationDtoToComment(commentRegistrationDto)));
    }

    @Override
    public Boolean deleteCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)){
            throw new ResourceNotFoundException("There is no comment with ID : "+commentId);
        }
        commentRepository.deleteById(commentId);
        return true;
    }

    @Override
    public Boolean deleteCommentByPostId(Long postId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        commentRepository.deleteByPostId(postId);
        return true;
    }

    @Override
    public Boolean deleteCommentByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        commentRepository.deleteByUserEmail(userEmail);
        return true;
    }

    @Override
    public CommentDto updateComment(Long commentId, Comment comment) {
        AtomicReference<Comment> updatedComment = new AtomicReference<>();

        commentRepository.findById(commentId).ifPresentOrElse(
                foundedComment->{
                    updateNonNullFields(foundedComment,comment);
                    updatedComment.set(commentRepository.save(foundedComment));
                },
                ()->{throw new ResourceNotFoundException("There is no comment with ID : "+commentId);}
        );
        return commentToCommentDto(updatedComment.get());
    }

    @Override
    public Page<CommentDto> getAllComment(Integer page , Integer size) {
        return commentRepository.findAll(validation.pageAndSizeValidation(page, size))
                .map(CommentMapper::commentToCommentDto);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return commentToCommentDto(commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no comment with ID : "+commentId)));
    }

    @Override
    public Page<CommentDto> getAllCommentByUserId(Long userId,Integer page , Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return commentRepository.findAllByUserId(userId,validation.pageAndSizeValidation(page, size))
                .map(CommentMapper::commentToCommentDto);
    }

    @Override
    public Page<CommentDto> getAllCommentByUserEmail(String userEmail,Integer page , Integer size) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        return commentRepository.findAllByUserEmail(userEmail,validation.pageAndSizeValidation(page,size))
                .map(CommentMapper::commentToCommentDto);
    }

    @Override
    public Page<CommentDto> getAllCommentByPostId(Long postId,Integer page , Integer size) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        return commentRepository.findAllByPostId(postId,validation.pageAndSizeValidation(page, size))
                .map(CommentMapper::commentToCommentDto);
    }

    private void updateNonNullFields(Comment updatedComment , Comment inputComment){
        if (inputComment.getId() != null) updatedComment.setId(inputComment.getId());

        if (inputComment.getContent() != null) updatedComment.setContent(inputComment.getContent());

        if (inputComment.getUser() != null) {
            Long userId = inputComment.getUser().getId();
            String userEmail = inputComment.getUser().getEmail();

            if (!userRepository.existsByEmail(userEmail)) {
                throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
            } else if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException("There is no user with ID : " + userId);
            }

            updatedComment.setUser(inputComment.getUser());
        }

        if (inputComment.getPost() != null) {

            Long postId = inputComment.getPost().getId();

            if (!postRepository.existsById(postId)) {
                throw new ResourceNotFoundException("There is no post with ID : "+postId);
            }

            updatedComment.setPost(inputComment.getPost());
        }
    }
}
