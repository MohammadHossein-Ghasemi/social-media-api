package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.NotificationRepository;
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

class NotificationServiceImplTest {

    private NotificationServiceImpl serviceUnderTest;

    @Mock
    Validation Validation;
    @Mock
    UserRepository userRepository;
    @Mock
    NotificationRepository notificationRepository;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        serviceUnderTest = new NotificationServiceImpl(notificationRepository,userRepository, Validation);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveNotif() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Notification notification = Notification.builder()
                .message("Test Message !!")
                .user(user)
                .build();

        when(Validation.isUserValid(user)).thenReturn(user);
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = serviceUnderTest.saveNotif(notification);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(notification);

        verify(notificationRepository).save(notification);
    }

    @Test
    void saveNotifWhenUserIsNotValid() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Notification notification = Notification.builder()
                .message("Test Message !!")
                .user(user)
                .build();

        when(Validation.isUserValid(user)).thenReturn(null);
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = serviceUnderTest.saveNotif(notification);

        assertThat(result).isNull();
    }

    @Test
    void deleteNotifById() {
        Long notifId = 14L;

        when(notificationRepository.existsById(notifId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteNotifById(notifId);

        assertThat(result).isTrue();

        verify(notificationRepository).deleteById(notifId);
    }

    @Test
    void deleteNotifByIdWhenUserIsNotExist() {
        Long notifId = 14L;

        when(notificationRepository.existsById(notifId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteNotifById(notifId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no notification with ID : " + notifId);
    }

    @Test
    void deleteNotifByUserId() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteNotifByUserId(userId);

        assertThat(result).isTrue();

        verify(notificationRepository).deleteByUserId(userId);
    }

    @Test
    void deleteNotifByUserIdWhenUserIsNotExist() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteNotifByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void deleteNotifByUserEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteNotifByUserEmail(userEmail);

        assertThat(result).isTrue();

        verify(notificationRepository).deleteByUserEmail(userEmail);
    }

    @Test
    void deleteNotifByUserEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteNotifByUserEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }

    @Test
    void updateNotifById() {
        Notification existingNotif = Notification
                .builder()
                .id(14L)
                .message("Test Message !!")
                .build();

        Notification updatedNotif = Notification
                .builder()
                .id(14L)
                .message("New Test Message !!")
                .build();

        when(notificationRepository.findById(existingNotif.getId())).thenReturn(Optional.of(existingNotif));
        when(notificationRepository.save(any(Notification.class))).thenReturn(existingNotif);

        Notification result = serviceUnderTest.updateNotifById(existingNotif.getId(), updatedNotif);

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo(updatedNotif.getMessage());

        verify(notificationRepository).save(existingNotif);
    }

    @Test
    void updateNotifByIdWhenNotifIsNotExist() {
        Notification existingNotif = Notification
                .builder()
                .id(14L)
                .message("Test Message !!")
                .build();

        Notification updatedNotif = Notification
                .builder()
                .id(14L)
                .message("New Test Message !!")
                .build();

        when(notificationRepository.findById(existingNotif.getId())).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenReturn(existingNotif);

        assertThatThrownBy(()->serviceUnderTest.updateNotifById(existingNotif.getId(), updatedNotif))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no post with ID : " + existingNotif.getId());
    }

    @Test
    void getNotifById() {
        Notification notification = Notification
                .builder()
                .id(14L)
                .message("Test Message !!")
                .build();

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));

        Notification result = serviceUnderTest.getNotifById(notification.getId());

        assertThat(result).isNotNull();

        verify(notificationRepository).findById(notification.getId());
    }

    @Test
    void getNotifByIdWhenNotifIsNotExist() {
        Notification notification = Notification
                .builder()
                .id(14L)
                .message("Test Message !!")
                .build();

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()->serviceUnderTest.getNotifById(notification.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no notification with ID : " + notification.getId());
    }

    @Test
    void getAllNotif() {
        serviceUnderTest.getAllNotif();
        verify(notificationRepository).findAll();
    }

    @Test
    void getAllNotifByUserId() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(true);

        List<Notification> result = serviceUnderTest.getAllNotifByUserId(userId);

        assertThat(result).isNotNull();

        verify(notificationRepository).findByUserId(userId);
    }

    @Test
    void getAllNotifByUserIdWhenUserIsNotExist() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllNotifByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void getAllNotifByUserEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        List<Notification> result = serviceUnderTest.getAllNotifByUserEmail(userEmail);

        assertThat(result).isNotNull();

        verify(notificationRepository).findByUserEmail(userEmail);
    }

    @Test
    void getAllNotifByUserEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllNotifByUserEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }
}