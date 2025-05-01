package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;
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

class PostServiceImplTest {

    private PostServiceImpl serviceUnderTest;

    @Mock
    private Validation validation;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    private AutoCloseable autoCloseable;

    private final int pageNumber = 0;
    private final int pageSize = 5;

    @BeforeEach
    void setUp(){
       autoCloseable = MockitoAnnotations.openMocks(this);
       serviceUnderTest = new PostServiceImpl(postRepository,userRepository,validation);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void savePost() {
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
        PostRegistrationDto postRegistrationDto = PostRegistrationDto.builder()
                .content("Test content!")
                .user(user)
                .build();

        when(validation.isUserValid(post.getUser())).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = serviceUnderTest.savePost(postRegistrationDto);

        assertThat(result).isNotNull();

        verify(postRepository).save(any(Post.class));
    }

    @Test
    void savePostWhenUseIsNotValid() {
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
        PostRegistrationDto postRegistrationDto = PostRegistrationDto.builder()
                .content("Test content!")
                .user(user)
                .build();

        when(validation.isUserValid(post.getUser())).thenReturn(null);
        when(postRepository.save(post)).thenReturn(post);

        PostDto result = serviceUnderTest.savePost(postRegistrationDto);

        assertThat(result).isNull();
    }

    @Test
    void deletePostById() {
        Long postId = 14L;
        when(postRepository.existsById(postId)).thenReturn(true);

        Boolean result = serviceUnderTest.deletePostById(postId);

        assertThat(result).isTrue();

        verify(postRepository).deleteById(postId);
    }

    @Test
    void deletePostByIdWhenPostIsNotExist() {
        Long postId = 14L;
        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deletePostById(postId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + postId);
    }

    @Test
    void updatePostById() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post existingPost = Post.builder()
                .id(14L)
                .content("Test Content!!")
                .user(user)
                .build();
        Post updatedPost = Post.builder()
                .id(14L)
                .content("New Test Content!!")
                .user(user)
                .build();

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        when(postRepository.findById(existingPost.getId())).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        PostDto result = serviceUnderTest.updatePostById(existingPost.getId(), updatedPost);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(updatedPost.getContent());

        verify(postRepository).save(existingPost);
    }

    @Test
    void updatePostByIdWhenPostIsNotExist() {
        Post existingPost = Post.builder()
                .id(14L)
                .content("Test Content!!")
                .build();
        Post updatedPost = Post.builder()
                .id(14L)
                .content("New Test Content!!")
                .build();

        when(postRepository.findById(existingPost.getId())).thenReturn(Optional.empty());
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        assertThatThrownBy(()->serviceUnderTest.updatePostById(updatedPost.getId(), updatedPost))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + updatedPost.getId());
    }

    @Test
    void getPostById() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Post post = Post.builder()
                .id(14L)
                .content("Test Content!!")
                .user(user)
                .build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        PostDto result = serviceUnderTest.getPostById(post.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(post.getId());

        verify(postRepository).findById(post.getId());
    }

    @Test
    void getPostByIdWhenUserIsNotExist() {
        Post post = Post.builder()
                .id(14L)
                .content("Test Content!!")
                .build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->serviceUnderTest.getPostById(post.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + post.getId());
    }

    @Test
    void getAllPost() {
        Page<Post> postPage = new PageImpl<>(List.of());
        when(postRepository.findAll(validation.pageAndSizeValidation(pageNumber,pageSize))).thenReturn(postPage);
        serviceUnderTest.getAllPost(pageNumber,pageSize);
        verify(postRepository).findAll(validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllPostByUserId() {
        Long userId = 14L;
        Page<Post> postPage = new PageImpl<>(List.of());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(postRepository.findByUserId(userId,validation.pageAndSizeValidation(pageNumber,pageSize))).thenReturn(postPage);

        Page<PostDto> result = serviceUnderTest.getAllPostByUserId(userId,pageNumber,pageSize);

        assertThat(result).isNotNull();

        verify(postRepository).findByUserId(userId,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllPostByUserIdWhenUserIsNotExist() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllPostByUserId(userId,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void getAllPostByUserEmail() {
        String userEmail = "test@example.com";

        Page<Post> postPage = new PageImpl<>(List.of());

        when(postRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize))).thenReturn(postPage);
        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        Page<PostDto> result = serviceUnderTest.getAllPostByUserEmail(userEmail,pageNumber,pageSize);

        assertThat(result).isNotNull();

        verify(postRepository).findByUserEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllPostByUserEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllPostByUserEmail(userEmail,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }
}