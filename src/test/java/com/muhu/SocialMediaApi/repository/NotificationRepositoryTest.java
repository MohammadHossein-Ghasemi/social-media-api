package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Notification;
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
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository repositoryUnderTest;

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

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        List<Notification> notificationList = repositoryUnderTest.findAll();

        assertThat(notificationList).isNotNull();
        assertThat(notificationList.size()).isEqualTo(1);
    }

    @Test
    void findById() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        Optional<Notification> foundedNotification = repositoryUnderTest.findById(savedNotification.getId());

        assertThat(foundedNotification).isPresent();
    }

    @Test
    void findByUserId() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        List<Notification> postList = repositoryUnderTest.findByUserId(savedUser.getId());

        assertThat(postList).isNotNull();
        assertThat(postList.size()).isEqualTo(1);
    }

    @Test
    void findByUserEmail(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        List<Notification> postList = repositoryUnderTest.findByUserEmail(savedUser.getEmail());

        assertThat(postList).isNotNull();
        assertThat(postList.size()).isEqualTo(1);
    }

    @Test
    void deleteByUserId(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        repositoryUnderTest.deleteByUserId(savedUser.getId());

        boolean result = repositoryUnderTest.existsById(savedNotification.getId());

        assertThat(result).isFalse();
    }

    @Test
    void deleteByUserEmail(){
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        Notification notification = Notification.builder()
                .message("Message Test.")
                .isRead(false)
                .user(savedUser)
                .build();

        Notification savedNotification = repositoryUnderTest.save(notification);

        assertThat(savedNotification).isNotNull();
        assertThat(savedNotification.getId()).isNotNull();

        repositoryUnderTest.deleteByUserEmail(savedUser.getEmail());

        boolean result = repositoryUnderTest.existsById(savedNotification.getId());

        assertThat(result).isFalse();
    }
}