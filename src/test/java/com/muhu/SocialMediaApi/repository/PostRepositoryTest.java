package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    PostRepository repositoryUnderTest;

    @Autowired
    UserRepository userRepository;

    private final PageRequest pageRequest = PageRequest.of(0,5);

    @Test
    void findAll() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        Post post = Post.builder()
                .content("Test Content.")
                .user(savedUser)
                .build();

        Post savedPost = repositoryUnderTest.save(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();

        List<Post> postList = repositoryUnderTest.findAll();

        assertThat(postList).isNotNull();
        assertThat(postList.size()).isEqualTo(1);
    }

    @Test
    void findById() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        Post post = Post.builder()
                .content("Test Content.")
                .user(savedUser)
                .build();

        Post savedPost = repositoryUnderTest.save(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();

        Optional<Post> foundedPost = repositoryUnderTest.findById(savedPost.getId());

        assertThat(foundedPost).isPresent();
    }

    @Test
    void findByUserId() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        Post post = Post.builder()
                .content("Test Content.")
                .user(savedUser)
                .build();

        Post savedPost = repositoryUnderTest.save(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();

        Page<Post> postList = repositoryUnderTest.findByUserId(savedUser.getId(),pageRequest);

        assertThat(postList).isNotNull();
        assertThat(postList.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByUserEmail(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        Post post = Post.builder()
                .content("Test Content.")
                .user(savedUser)
                .build();

        Post savedPost = repositoryUnderTest.save(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();

        Page<Post> postList = repositoryUnderTest.findByUserEmail(savedUser.getEmail(),pageRequest);

        assertThat(postList).isNotNull();
        assertThat(postList.getTotalElements()).isEqualTo(1);
    }
}