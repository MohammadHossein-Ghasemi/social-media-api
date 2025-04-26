package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.CommentRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    private CommentServiceImpl serviceUnderTest;
    private AutoCloseable autoCloseable;

    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    Validation validation;

    @BeforeEach
    void setUp(){
        autoCloseable= MockitoAnnotations.openMocks(this);
        serviceUnderTest=new CommentServiceImpl(commentRepository,userRepository,postRepository,validation);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveComment() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("Test Content !!")
                .build();

        when(validation.isPostValid(post)).thenReturn(true);
        when(validation.isUserValid(user)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = serviceUnderTest.saveComment(comment);

        assertThat(result).isNotNull();

        verify(commentRepository).save(comment);
    }

    @Test
    void saveCommentWhenPostIsNotValid() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("Test Content !!")
                .build();

        when(validation.isPostValid(post)).thenReturn(false);
        when(validation.isUserValid(user)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = serviceUnderTest.saveComment(comment);

        assertThat(result).isNull();
    }

    @Test
    void saveCommentWhenUserIsNotValid() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("Test Content !!")
                .build();

        when(validation.isPostValid(post)).thenReturn(true);
        when(validation.isUserValid(user)).thenReturn(false);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = serviceUnderTest.saveComment(comment);

        assertThat(result).isNull();
    }

    @Test
    void saveCommentWhenPostAndUserAreNotValid() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("Test Content !!")
                .build();

        when(validation.isPostValid(post)).thenReturn(false);
        when(validation.isUserValid(user)).thenReturn(false);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = serviceUnderTest.saveComment(comment);

        assertThat(result).isNull();
    }


    @Test
    void deleteCommentById() {
        Long commentId = 14L;

        when(commentRepository.existsById(commentId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteCommentById(commentId);

        assertThat(result).isTrue();

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void deleteCommentByIdWhenCommentIsNotExist() {
        Long commentId = 14L;

        when(commentRepository.existsById(commentId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteCommentById(commentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no comment with ID : " + commentId);
    }

    @Test
    void deleteCommentByPostId() {
        Long postId = 14L;

        when(postRepository.existsById(postId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteCommentByPostId(postId);

        assertThat(result).isTrue();

        verify(commentRepository).deleteByPostId(postId);
    }

    @Test
    void deleteCommentByIdWhenPostIsNotExist() {
        Long postId = 14L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteCommentByPostId(postId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + postId);
    }

    @Test
    void deleteCommentByUserEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteCommentByUserEmail(userEmail);

        assertThat(result).isTrue();

        verify(commentRepository).deleteByUserEmail(userEmail);
    }

    @Test
    void deleteCommentByIdWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteCommentByUserEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }

    @Test
    void updateComment() {
        Comment existingComment = Comment.builder()
                .id(14L)
                .content("Test Content !!")
                .build();
        Comment updatedComment = Comment.builder()
                .id(14L)
                .content("New Test Content !!")
                .build();

        when(commentRepository.findById(existingComment.getId())).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        Comment result = serviceUnderTest.updateComment(existingComment.getId(), updatedComment);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(updatedComment.getContent());

        verify(commentRepository).save(existingComment);
    }

    @Test
    void updateCommentWhenCommentIsNotExist() {
        Comment existingComment = Comment.builder()
                .id(14L)
                .content("Test Content !!")
                .build();
        Comment updatedComment = Comment.builder()
                .id(14L)
                .content("New Test Content !!")
                .build();

        when(commentRepository.findById(existingComment.getId())).thenReturn(Optional.empty());
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        assertThatThrownBy(()->serviceUnderTest.updateComment(existingComment.getId(), updatedComment))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no comment with ID : " + existingComment.getId());
    }

    @Test
    void getAllComment() {
        serviceUnderTest.getAllComment();
        verify(commentRepository).findAll();
    }

    @Test
    void getCommentById() {
        Comment comment = Comment.builder()
                .id(14L)
                .content("Test Content !!")
                .build();
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Comment result = serviceUnderTest.getCommentById(comment.getId());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(comment);

        verify(commentRepository).findById(comment.getId());
    }

    @Test
    void getCommentByIdWhenCommentIsNotExist() {
        Comment comment = Comment.builder()
                .id(14L)
                .content("Test Content !!")
                .build();
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->serviceUnderTest.getCommentById(comment.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no comment with ID : " + comment.getId());

    }

    @Test
    void getAllCommentByUserId() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(true);

        List<Comment> result = serviceUnderTest.getAllCommentByUserId(userId);

        assertThat(result).isNotNull();

        verify(commentRepository).findAllByUserId(userId);
    }

    @Test
    void getAllCommentByUserIdWhenUserIsNotExist() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllCommentByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void getAllCommentByUserEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        List<Comment> result = serviceUnderTest.getAllCommentByUserEmail(userEmail);

        assertThat(result).isNotNull();

        verify(commentRepository).findAllByUserEmail(userEmail);
    }

    @Test
    void getAllCommentByUserEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllCommentByUserEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }

    @Test
    void getAllCommentByPostId() {
        Long postId = 14L;

        when(postRepository.existsById(postId)).thenReturn(true);

        List<Comment> result = serviceUnderTest.getAllCommentByPostId(postId);

        assertThat(result).isNotNull();

        verify(commentRepository).findAllByPostId(postId);
    }

    @Test
    void getAllCommentByPostIdWhenPostIsNotExist() {
        Long postId = 14L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllCommentByPostId(postId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + postId);
    }
}