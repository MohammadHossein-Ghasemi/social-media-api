package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.CommentRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public Comment saveComment(Comment comment) {
        if (!userValidation(comment.getUser()) || !postValidation(comment.getPost())) {
            return null;
        }
        return commentRepository.save(comment);
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
    public Comment updateComment(Long commentId, Comment comment) {
        AtomicReference<Comment> updatedComment = new AtomicReference<>();

        commentRepository.findById(commentId).ifPresentOrElse(
                foundedComment->{
                    updateNonNullFields(foundedComment,comment);
                    updatedComment.set(commentRepository.save(foundedComment));
                },
                ()->{throw new ResourceNotFoundException("There is no comment with ID : "+commentId);}
        );
        return updatedComment.get();
    }

    @Override
    public List<Comment> getAllComment() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no comment with ID : "+commentId));
    }

    @Override
    public List<Comment> getAllCommentByUserId(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return commentRepository.findAllByUserId(userId);
    }

    @Override
    public List<Comment> getAllCommentByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        return commentRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public List<Comment> getAllCommentByPostId(Long postId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("There is no post with ID : "+postId);
        }
        return commentRepository.findAllByPostId(postId);
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
