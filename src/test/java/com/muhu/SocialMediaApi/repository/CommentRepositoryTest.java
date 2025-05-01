package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    CommentRepository repositoryUnderTest;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    private final Pageable pageable = PageRequest.of(0,5);

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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        List<Comment> commentList = repositoryUnderTest.findAll();

        assertThat(commentList).isNotNull();
        assertThat(commentList.size()).isEqualTo(1);
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        Optional<Comment> foundedComment = repositoryUnderTest.findById(savedComment.getId());

        assertThat(foundedComment).isPresent();
    }

    @Test
    void findAllByUserId() {
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        Page<Comment> commentList = repositoryUnderTest.findAllByUserId(savedUser.getId(),pageable);

        assertThat(commentList).isNotNull();
        assertThat(commentList.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllByUserEmail(){
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        Page<Comment> commentList = repositoryUnderTest.findAllByUserEmail(savedUser.getEmail(),pageable);

        assertThat(commentList).isNotNull();
        assertThat(commentList.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllByPostId() {
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        Page<Comment> commentList = repositoryUnderTest.findAllByPostId(savedPost.getId(),pageable);

        assertThat(commentList).isNotNull();
        assertThat(commentList.getTotalElements()).isEqualTo(1);
    }

    @Test
    void deleteByUserEmail(){
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        repositoryUnderTest.deleteByUserEmail(savedUser.getEmail());

        boolean result = repositoryUnderTest.existsById(savedComment.getId());

        assertThat(result).isFalse();
    }

    @Test
    void deleteByPostId(){
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

        Comment comment = Comment.builder()
                .content("Test Comment .")
                .post(savedPost)
                .user(savedUser)
                .build();

        Comment savedComment = repositoryUnderTest.save(comment);
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();

        repositoryUnderTest.deleteByPostId(savedPost.getId());

        boolean result = repositoryUnderTest.existsById(savedComment.getId());

        assertThat(result).isFalse();
    }
}