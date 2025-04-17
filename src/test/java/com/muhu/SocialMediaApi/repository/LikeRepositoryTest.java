package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeRepositoryTest {

    @Autowired
    LikeRepository repositoryUnderTest;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

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

        Post savedPost = postRepository.save(post);

        Like like = Like.builder()
                .post(savedPost)
                .user(savedUser)
                .build();

        Like savedLike = repositoryUnderTest.save(like);
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getId()).isNotNull();

        List<Like> likeList = repositoryUnderTest.findAll();

        assertThat(likeList).isNotNull();
        assertThat(likeList.size()).isEqualTo(1);
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

        Post savedPost = postRepository.save(post);

        Like like = Like.builder()
                .post(savedPost)
                .user(savedUser)
                .build();

        Like savedLike = repositoryUnderTest.save(like);
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getId()).isNotNull();

        Optional<Like> foundedLike = repositoryUnderTest.findById(savedLike.getId());

        assertThat(foundedLike).isPresent();

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

        Post savedPost = postRepository.save(post);

        Like like = Like.builder()
                .post(savedPost)
                .user(savedUser)
                .build();

        Like savedLike = repositoryUnderTest.save(like);
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getId()).isNotNull();

        List<Like> likeList = repositoryUnderTest.findByUserId(savedUser.getId());

        assertThat(likeList).isNotNull();
        assertThat(likeList.size()).isEqualTo(1);
    }

    @Test
    void findByPostId() {
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

        Post savedPost = postRepository.save(post);

        Like like = Like.builder()
                .post(savedPost)
                .user(savedUser)
                .build();

        Like savedLike = repositoryUnderTest.save(like);
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getId()).isNotNull();

        List<Like> likeList = repositoryUnderTest.findByPostId(savedPost.getId());

        assertThat(likeList).isNotNull();
        assertThat(likeList.size()).isEqualTo(1);
    }
}