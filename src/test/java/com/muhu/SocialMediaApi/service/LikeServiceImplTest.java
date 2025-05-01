package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;
import com.muhu.SocialMediaApi.repository.LikeRepository;
import com.muhu.SocialMediaApi.repository.PostRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LikeServiceImplTest {

    private LikeServiceImpl serviceUnderTest;
    private AutoCloseable autoCloseable;

    @Mock
    Validation validation;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    LikeRepository likeRepository;

    private final int pageNumber=0;
    private final int pageSize=5;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        serviceUnderTest = new LikeServiceImpl(likeRepository,userRepository,postRepository,validation);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveLike() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Like like = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto.builder()
                .user(user)
                .post(post)
                .build();

        when(validation.isUserValid(user)).thenReturn(user);
        when(validation.isPostValid(post)).thenReturn(post);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        LikeDto result = serviceUnderTest.saveLike(likeRegistrationDto);

        assertThat(result).isNotNull();

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void saveLikeWhenUserIsNotValid() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Like like = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto.builder()
                .user(user)
                .post(post)
                .build();

        when(validation.isUserValid(user)).thenReturn(null);
        when(validation.isPostValid(post)).thenReturn(post);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        LikeDto result = serviceUnderTest.saveLike(likeRegistrationDto);

        assertThat(result).isNull();
    }

    @Test
    void saveLikeWhenPostIsNotValid() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Like like = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto.builder()
                .user(user)
                .post(post)
                .build();

        when(validation.isUserValid(user)).thenReturn(user);
        when(validation.isPostValid(post)).thenReturn(null);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        LikeDto result = serviceUnderTest.saveLike(likeRegistrationDto);

        assertThat(result).isNull();
    }

    @Test
    void saveLikeWhenUserAndPostAreNotValid() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .content("Test content!")
                .user(user)
                .build();
        Like like = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto.builder()
                .user(user)
                .post(post)
                .build();

        when(validation.isUserValid(user)).thenReturn(null);
        when(validation.isPostValid(post)).thenReturn(null);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        LikeDto result = serviceUnderTest.saveLike(likeRegistrationDto);

        assertThat(result).isNull();
    }

    @Test
    void deleteLikeById() {
        Long likeId = 14L;

        when(likeRepository.existsById(likeId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteLikeById(likeId);

        assertThat(result).isTrue();

        verify(likeRepository).deleteById(likeId);
    }

    @Test
    void deleteLikeByIdWhenLikeIsNotExist() {
        Long likeId = 14L;

        when(likeRepository.existsById(likeId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteLikeById(likeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no like with ID : "+likeId);
    }

    @Test
    void updateLike() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .id(14L)
                .content("Test content!")
                .user(user)
                .build();
        Like existingLike = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();
        user.setUsername("new User Name !!");
        Like updatedLike = Like.builder()
                .id(14L)
                .user(user)
                .post(post)
                .build();

        when(postRepository.existsById(post.getId())).thenReturn(true);
        when(likeRepository.findById(existingLike.getId())).thenReturn(Optional.of(existingLike));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(likeRepository.save(any(Like.class))).thenReturn(existingLike);

        LikeDto result = serviceUnderTest.updateLike(existingLike.getId(), updatedLike);

        assertThat(result).isNotNull();
        assertThat(result.getUser().getUsername()).isEqualTo(updatedLike.getUser().getUsername());

        verify(likeRepository).findById(existingLike.getId());
        verify(likeRepository).save(existingLike);

    }

    @Test
    void updateLikeWhenLikeIsNotExist() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Like existingLike = Like.builder()
                .id(14L)
                .user(user)
                .build();
        user.setUsername("new User Name !!");
        Like updatedLike = Like.builder()
                .id(14L)
                .user(user)
                .build();

        when(likeRepository.findById(existingLike.getId())).thenReturn(Optional.empty());
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(likeRepository.save(any(Like.class))).thenReturn(existingLike);

        assertThatThrownBy(()->serviceUnderTest.updateLike(existingLike.getId(), updatedLike))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no like with ID : " + updatedLike.getId());

    }

    @Test
    void getLikeByID() {
        Long likeId = 14L;

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

        Like like = Like.builder()
                .id(likeId)
                .post(post)
                .user(user)
                .build();

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));

        LikeDto result = serviceUnderTest.getLikeByID(likeId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(likeId);

        verify(likeRepository).findById(likeId);
    }

    @Test
    void getLikeByIDWhenUserIsNotExist() {
        Long likeId = 14L;

        Like like = Like.builder()
                .id(likeId)
                .build();

        when(likeRepository.findById(likeId)).thenReturn(Optional.empty());

        assertThatThrownBy(()->serviceUnderTest.getLikeByID(likeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no like with ID : " + likeId);
    }

    @Test
    void getAllLike() {
        Page<Like> likePage = new PageImpl<>(List.of());
        when(likeRepository.findAll(validation.pageAndSizeValidation(pageNumber,pageSize))).thenReturn(likePage);
        serviceUnderTest.getAllLike(pageNumber,pageSize);
        verify(likeRepository).findAll(validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllLikeByUserId() {
        Long userId = 14L;
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByUserId(userId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(userRepository.existsById(userId)).thenReturn(true);

        Page<LikeDto> result = serviceUnderTest.getAllLikeByUserId(userId,pageNumber,pageSize);

        assertThat(result).isNotNull();

        verify(likeRepository).findByUserId(userId,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllLikeByUserIdWhenUserIsNotExist() {
        Long userId = 14L;
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByUserId(userId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllLikeByUserId(userId,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void getAllLikeByUserEmail() {
        String userEmail = "test@example.com";
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        Page<LikeDto> result = serviceUnderTest.getAllLikeByUserEmail(userEmail,pageNumber,pageSize);

        assertThat(result).isNotNull();

        verify(likeRepository).findByUserEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllLikeByUserEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllLikeByUserEmail(userEmail,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }

    @Test
    void getAllLikeByPostId() {
        Long postId = 14L;
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByPostId(postId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(postRepository.existsById(postId)).thenReturn(true);

        Page<LikeDto> result = serviceUnderTest.getAllLikeByPostId(postId,pageNumber,pageSize);

        assertThat(result).isNotNull();

        verify(likeRepository).findByPostId(postId,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllLikeByPostIdWhenPostIsNotExist() {
        Long postId = 14L;
        Page<Like> likePage = new PageImpl<>(List.of());

        when(likeRepository.findByPostId(postId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(likePage);
        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllLikeByPostId(postId,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + postId);
    }
}