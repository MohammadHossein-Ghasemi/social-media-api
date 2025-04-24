package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.DuplicateException;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {
    private UserServiceImpl serviceUnderTest;
    private AutoCloseable autoCloseable;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
       autoCloseable = MockitoAnnotations.openMocks(this);
       serviceUnderTest = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveUser() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = serviceUnderTest.saveUser(user);

        assertThat(result).isNotNull();

        verify(userRepository).save(user);
    }
    @Test
    void saveUserWhenUserIsDuplicate() {
        User user = User.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThatThrownBy(()->serviceUnderTest.saveUser(user))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("This user is already exists.");
    }

    @Test
    void deleteUserById() {
        Long userId = 14L;
        when(userRepository.existsById(userId)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteUserById(userId);

        assertThat(result).isTrue();

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserByIdWhenUserIsNotExist() {
        Long userId = 14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : " + userId);
    }

    @Test
    void deleteUserByEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        Boolean result = serviceUnderTest.deleteUserByEmail(userEmail);

        assertThat(result).isTrue();

        verify(userRepository).deleteByEmail(userEmail);
    }

    @Test
    void deleteUserByEmailWhenUserIsNotExist() {
        String userEmail = "test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.deleteUserByEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + userEmail);
    }

    @Test
    void updateUser() {
        User existingUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("muhu")
                .build();

        User updatedUser= User.builder()
                .id(1L)
                .username("new username")
                .build();

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);


        User result = serviceUnderTest.updateUser(existingUser.getEmail(), updatedUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());

        verify(userRepository).findByEmail(existingUser.getEmail());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUserWhenUserNotFound() {
        User existingUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("muhu")
                .build();

        User updatedUser= User.builder()
                .id(1L)
                .username("new username")
                .build();

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> serviceUnderTest.updateUser(existingUser.getEmail(),updatedUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : " + existingUser.getEmail()+
                        " and ID : "+existingUser.getId());
    }

    @Test
    void getAllUser() {
        serviceUnderTest.getAllUser();
        verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = serviceUnderTest.getUserById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(user);

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getUserByIdWhenUserIsNotExist() {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());


        assertThatThrownBy(()->serviceUnderTest.getUserById(user.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : "+user.getId());
    }

    @Test
    void getUserByEmail() {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = serviceUnderTest.getUserByEmail(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(user);

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getUserByEmailWhenUserIsNotExist() {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());


        assertThatThrownBy(()->serviceUnderTest.getUserByEmail(user.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : "+user.getEmail());
    }

    @Test
    void getAllUserFollowersById() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(true);

        serviceUnderTest.getAllUserFollowersById(userId);

        verify(userRepository).findAllFollowersById(userId);
    }

    @Test
    void getAllUserFollowersByIdWhenUserIsNotExist() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowersById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : "+userId);
    }

    @Test
    void getAllUserFollowingById() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(true);

        serviceUnderTest.getAllUserFollowingById(userId);

        verify(userRepository).findAllFollowingById(userId);
    }

    @Test
    void getAllUserFollowingByIdWhenUserIsNotExist() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowingById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : "+userId);
    }

    @Test
    void getAllUserFollowersByEmail() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        serviceUnderTest.getAllUserFollowersByEmail(userEmail);

        verify(userRepository).findAllFollowersByEmail(userEmail);
    }

    @Test
    void getAllUserFollowersByEmailWhenUserIsNotExist() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowersByEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : "+userEmail);
    }

    @Test
    void getAllUserFollowingByEmail() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        serviceUnderTest.getAllUserFollowingByEmail(userEmail);

        verify(userRepository).findAllFollowingByEmail(userEmail);
    }

    @Test
    void getAllUserFollowingByEmailWhenUserIsNotExist() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowingByEmail(userEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : "+userEmail);
    }

    @Test
    void updateNonNullFields(){
    }
}