package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Authority;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.DuplicateException;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.model.UserRegistrationDto;
import com.muhu.SocialMediaApi.repository.AuthorityRepository;
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


class UserServiceImplTest {
    private UserServiceImpl serviceUnderTest;
    private AutoCloseable autoCloseable;

    @Mock
    private UserRepository userRepository;
    @Mock
    private Validation validation;
    @Mock
    private AuthorityRepository authorityRepository;

    private final int pageSize=5;
    private final int pageNumber=0;

    @BeforeEach
    void setUp(){
       autoCloseable = MockitoAnnotations.openMocks(this);
       serviceUnderTest = new UserServiceImpl(userRepository,validation,authorityRepository);
    }

    @AfterEach
    void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveUser() {
        User user = User.builder()
                .id(14L)
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();
        Authority authority = Authority.builder()
                .user(user)
                .name("ROLE_USER")
                .build();

        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authorityRepository.save(any(Authority.class))).thenReturn(authority);

        UserDto result = serviceUnderTest.saveUser(userRegistrationDto);

        assertThat(result).isNotNull();

        verify(userRepository).save(any(User.class));
    }
    @Test
    void saveUserWhenUserIsDuplicate() {
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .username("muhu")
                .email("muhu@emaple.com")
                .password("asfbbgfbgnhmj,hgmhng")
                .build();

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        assertThatThrownBy(()->serviceUnderTest.saveUser(userRegistrationDto))
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


        UserDto result = serviceUnderTest.updateUser(existingUser.getEmail(), updatedUser);

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
        Page<User> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(validation.pageAndSizeValidation(pageNumber,pageSize))).thenReturn(userPage);
        serviceUnderTest.getAllUser(pageNumber,pageSize);
        verify(userRepository).findAll(validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getUserById() {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto result = serviceUnderTest.getUserById(user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());

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

        UserDto result = serviceUnderTest.getUserByEmail(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());

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

        Page<User> userPage = new PageImpl<>(List.of());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findAllFollowersById(userId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(userPage);

        serviceUnderTest.getAllUserFollowersById(userId,pageNumber,pageSize);

        verify(userRepository).findAllFollowersById(userId,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllUserFollowersByIdWhenUserIsNotExist() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowersById(userId,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : "+userId);
    }

    @Test
    void getAllUserFollowingById() {
        Long userId=14L;

        Page<User> userPage = new PageImpl<>(List.of());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findAllFollowingById(userId,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(userPage);

        serviceUnderTest.getAllUserFollowingById(userId,pageNumber,pageSize);

        verify(userRepository).findAllFollowingById(userId,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllUserFollowingByIdWhenUserIsNotExist() {
        Long userId=14L;

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowingById(userId,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with ID : "+userId);
    }

    @Test
    void getAllUserFollowersByEmail() {
        String userEmail="test@example.com";

        Page<User> userPage = new PageImpl<>(List.of());

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);
        when(userRepository.findAllFollowersByEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(userPage);

        serviceUnderTest.getAllUserFollowersByEmail(userEmail,pageNumber,pageSize);

        verify(userRepository).findAllFollowersByEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllUserFollowersByEmailWhenUserIsNotExist() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowersByEmail(userEmail,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : "+userEmail);
    }

    @Test
    void getAllUserFollowingByEmail() {
        String userEmail="test@example.com";

        Page<User> userPage = new PageImpl<>(List.of());

        when(userRepository.existsByEmail(userEmail)).thenReturn(true);
        when(userRepository.findAllFollowingByEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize)))
                .thenReturn(userPage);

        serviceUnderTest.getAllUserFollowingByEmail(userEmail,pageNumber,pageSize);

        verify(userRepository).findAllFollowingByEmail(userEmail,validation.pageAndSizeValidation(pageNumber,pageSize));
    }

    @Test
    void getAllUserFollowingByEmailWhenUserIsNotExist() {
        String userEmail="test@example.com";

        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        assertThatThrownBy(()->serviceUnderTest.getAllUserFollowingByEmail(userEmail,pageNumber,pageSize))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There is no user with Email : "+userEmail);
    }

    @Test
    void updateNonNullFields(){
    }
}