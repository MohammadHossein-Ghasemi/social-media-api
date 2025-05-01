package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository repositoryUnderTest;

    PageRequest pageRequest = PageRequest.of(0,5);

    @Test
    void findAll() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        List<User> userList = repositoryUnderTest.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    void findById() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> foundedUser = repositoryUnderTest.findById(savedUser.getId());

        assertThat(foundedUser).isPresent();
    }

    @Test
    void findByUsername() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> foundedUser = repositoryUnderTest.findByUsername(savedUser.getUsername());

        assertThat(foundedUser).isPresent();
    }

    @Test
    void findByEmail() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Optional<User> foundedUser = repositoryUnderTest.findByEmail(savedUser.getEmail());

        assertThat(foundedUser).isPresent();
    }

    @Test
    void findAllFollowersById() {
        User follower = User.builder()
                .username("muhu_1")
                .email("muhu_1@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedFollower = repositoryUnderTest.save(follower);

        assertThat(savedFollower).isNotNull();
        assertThat(savedFollower.getId()).isNotNull();

        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .followers(Set.of(savedFollower))
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Page<User> userFollowers = repositoryUnderTest.findAllFollowersById(savedUser.getId(),pageRequest);

        assertThat(userFollowers).isNotNull();
        assertThat(userFollowers.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllFollowingById() {
        User following = User.builder()
                .username("muhu_1")
                .email("muhu_1@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedFollowing = repositoryUnderTest.save(following);

        assertThat(savedFollowing).isNotNull();
        assertThat(savedFollowing.getId()).isNotNull();

        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .following(Set.of(savedFollowing))
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Page<User> userFollowing = repositoryUnderTest.findAllFollowingById(savedUser.getId(),pageRequest);

        assertThat(userFollowing).isNotNull();
        assertThat(userFollowing.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllFollowersByEmail(){
        User follower = User.builder()
                .username("muhu_1")
                .email("muhu_1@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedFollower = repositoryUnderTest.save(follower);

        assertThat(savedFollower).isNotNull();
        assertThat(savedFollower.getId()).isNotNull();

        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .followers(Set.of(savedFollower))
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Page<User> userFollowers = repositoryUnderTest.findAllFollowersByEmail(savedUser.getEmail(),pageRequest);

        assertThat(userFollowers).isNotNull();
        assertThat(userFollowers.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllFollowingByEmail(){
        User following = User.builder()
                .username("muhu_1")
                .email("muhu_1@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedFollowing = repositoryUnderTest.save(following);

        assertThat(savedFollowing).isNotNull();
        assertThat(savedFollowing.getId()).isNotNull();

        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .following(Set.of(savedFollowing))
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Page<User> userFollowing = repositoryUnderTest.findAllFollowingByEmail(savedUser.getEmail(),pageRequest);

        assertThat(userFollowing).isNotNull();
        assertThat(userFollowing.getTotalElements()).isEqualTo(1);
    }

    @Test
    void existsByEmail(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        boolean result = repositoryUnderTest.existsByEmail(user.getEmail());

        assertThat(result).isTrue();
    }

    @Test
    void deleteByEmail(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = repositoryUnderTest.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        repositoryUnderTest.deleteByEmail(savedUser.getEmail());

        boolean result = repositoryUnderTest.existsByEmail(user.getEmail());

        assertThat(result).isFalse();
    }
}