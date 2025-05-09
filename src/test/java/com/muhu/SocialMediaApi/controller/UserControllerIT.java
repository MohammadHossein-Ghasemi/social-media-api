package com.muhu.SocialMediaApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.LoginRequestDto;
import com.muhu.SocialMediaApi.model.UserDto;
import com.muhu.SocialMediaApi.model.UserRegistrationDto;
import com.muhu.SocialMediaApi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Environment environment;


    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private PasswordEncoder passwordEncoder;


    @Test
    void userRegistration() throws Exception {
        String rawPassword = "testPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("muhu");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword(rawPassword);

        UserDto savedUser = new UserDto();
        savedUser.setUsername("muhu");

        when(userService.saveUser(any(UserRegistrationDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/register"))
                .andExpect(jsonPath("$.data.username").value("muhu"));

        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userService, times(1)).saveUser(any(UserRegistrationDto.class));
    }

    @Test
    void userRegistrationWhenRegistrationFailed() throws Exception {
        String rawPassword = "testPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("muhu");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword(rawPassword);

        when(userService.saveUser(any(UserRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/register"))
                .andExpect(content().string("There is problem on register user"));

        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userService, times(1)).saveUser(any(UserRegistrationDto.class));
    }

    @Test
    void deleteUserById() throws Exception {
        Long userId = 14L;

        when(userService.deleteUserById(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/user/delete/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/delete"))
                .andExpect(content().string("The user with ID 14 was deleted."));

        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    void deleteUserByIdWhenUserNotFound() throws Exception {
        Long userId = 14L;

        when(userService.deleteUserById(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/user/delete/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/delete"));

        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    void deleteUserByEmail() throws Exception {
        String userEmail = "test@example.com";

        when(userService.deleteUserByEmail(userEmail)).thenReturn(true);

        mockMvc.perform(delete("/api/user/delete")
                        .param("userEmail",userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/delete"))
                .andExpect(content().string("The user with Email "+userEmail+" was deleted."));

        verify(userService, times(1)).deleteUserByEmail(userEmail);
    }

    @Test
    void deleteUserByEmailWhenUSerNotFound() throws Exception {
        String userEmail = "test@example.com";

        when(userService.deleteUserByEmail(userEmail)).thenReturn(false);

        mockMvc.perform(delete("/api/user/delete")
                        .param("userEmail",userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/delete"))
                .andExpect(content().string("There is problem on deleting user"));

        verify(userService, times(1)).deleteUserByEmail(userEmail);
    }

    @Test
    void updateUser() throws Exception {
        String userEmail = "test@example.com";

        User user = new User();
        user.setUsername("updatedName");
        user.setPassword("newPassword");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("updatedName");
        userDto.setEmail(userEmail);

        when(userService.updateUser(any(String.class), any(User.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/user/update")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/update"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User updated successfully."))
                .andExpect(jsonPath("$.data.username").value("updatedName"));

        verify(userService, times(1)).updateUser(any(String.class), any(User.class));
    }

    @Test
    void updateUserWhenUserNotFound() throws Exception {
        String userEmail = "test@example.com";

        User user = new User();
        user.setUsername("updatedName");
        user.setPassword("newPassword");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("updatedName");
        userDto.setEmail(userEmail);

        when(userService.updateUser(any(String.class), any(User.class))).thenReturn(null);

        mockMvc.perform(put("/api/user/update")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/update"))
                .andExpect(content().string("There is problem on updating user"));

        verify(userService, times(1)).updateUser(any(String.class), any(User.class));
    }

    @Test
    void getAllUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(14L);
        userDto.setUsername("muhu");

        Page<UserDto> userPage = new PageImpl<>(List.of(userDto));

        when(userService.getAllUser(0,0)).thenReturn(userPage);

        mockMvc.perform(get("/api/user/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/all"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("muhu"));

        verify(userService, times(1)).getAllUser(0, 0);
    }

    @Test
    void getAllUserWhenThereIsNoUser() throws Exception {
        Page<UserDto> userPage = new PageImpl<>(List.of());

        when(userService.getAllUser(0,0)).thenReturn(userPage);

        mockMvc.perform(get("/api/user/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/all"))
                .andExpect(content().string("There is no user to display."));

        verify(userService, times(1)).getAllUser(0, 0);
    }

    @Test
    void getUserById() throws Exception {
        Long userId = 14L;

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setUsername("muhu");
        userDto.setEmail("test@example.com");

        when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/" + userId))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User founded successfully."))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserByEmail() throws Exception {
        String userEmail = "test@example.com";

        UserDto userDto = new UserDto();
        userDto.setId(14L);
        userDto.setUsername("muhu");
        userDto.setEmail(userEmail);

        when(userService.getUserByEmail(userEmail)).thenReturn(userDto);

        mockMvc.perform(get("/api/user")
                        .param("userEmail",userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User founded successfully."))
                .andExpect(jsonPath("$.data.email").value(userEmail));

        verify(userService, times(1)).getUserByEmail(userEmail);
    }

    @Test
    void getAllUserFollowersById() throws Exception {
        Long userId = 14L;

        UserDto follower = new UserDto();
        follower.setId(14L);
        follower.setUsername("muhu");

        Page<UserDto> followerPage = new PageImpl<>(List.of(follower));

        when(userService.getAllUserFollowersById(userId, 0, 0)).thenReturn(followerPage);

        mockMvc.perform(get("/api/user/followers/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/followers"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("muhu"));

        verify(userService, times(1)).getAllUserFollowersById(userId, 0, 0);
    }

    @Test
    void getAllUserFollowersByIdWhenThereIsNoUser() throws Exception {
        Long userId = 14L;

        UserDto follower = new UserDto();
        follower.setId(14L);
        follower.setUsername("muhu");

        Page<UserDto> followerPage = new PageImpl<>(List.of());

        when(userService.getAllUserFollowersById(userId, 0, 0)).thenReturn(followerPage);

        mockMvc.perform(get("/api/user/followers/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/followers"));

        verify(userService, times(1)).getAllUserFollowersById(userId, 0, 0);
    }

    @Test
    void getAllUserFollowingById() throws Exception {
        Long userId = 14L;

        UserDto following = new UserDto();
        following.setId(14L);
        following.setUsername("muhu");

        Page<UserDto> followingPage = new PageImpl<>(List.of(following));

        when(userService.getAllUserFollowingById(userId, 0, 0)).thenReturn(followingPage);

        mockMvc.perform(get("/api/user/following/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/following"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("muhu"));

        verify(userService, times(1)).getAllUserFollowingById(userId, 0, 0);
    }

    @Test
    void getAllUserFollowingByIdWhenThereIsNoFollowing() throws Exception {
        Long userId = 14L;

        UserDto following = new UserDto();
        following.setId(14L);
        following.setUsername("muhu");

        Page<UserDto> followingPage = new PageImpl<>(List.of());

        when(userService.getAllUserFollowingById(userId, 0, 0)).thenReturn(followingPage);

        mockMvc.perform(get("/api/user/following/{userId}", userId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/following"));

        verify(userService, times(1)).getAllUserFollowingById(userId, 0, 0);
    }

    @Test
    void getAllUserFollowersByEmail() throws Exception {
        String userEmail = "test@example.com";

        UserDto follower = new UserDto();
        follower.setId(14L);
        follower.setUsername("muhu");

        Page<UserDto> followerPage = new PageImpl<>(List.of(follower));

        when(userService.getAllUserFollowersByEmail(userEmail, 0, 0)).thenReturn(followerPage);

        mockMvc.perform(get("/api/user/followers")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/followers"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("muhu"));

        verify(userService, times(1)).getAllUserFollowersByEmail(userEmail, 0, 0);
    }

    @Test
    void getAllUserFollowersByEmailWhenThereIsNoUser() throws Exception {
        String userEmail = "test@example.com";

        Page<UserDto> followerPage = new PageImpl<>(List.of());

        when(userService.getAllUserFollowersByEmail(userEmail, 0, 0)).thenReturn(followerPage);

        mockMvc.perform(get("/api/user/followers")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/followers"));

        verify(userService, times(1)).getAllUserFollowersByEmail(userEmail, 0, 0);
    }

    @Test
    void getAllUserFollowingByEmail() throws Exception {
        String userEmail = "test@example.com";

        UserDto follower = new UserDto();
        follower.setId(14L);
        follower.setUsername("muhu");

        Page<UserDto> followingPage = new PageImpl<>(List.of(follower));

        when(userService.getAllUserFollowingByEmail(userEmail, 0, 0)).thenReturn(followingPage);

        mockMvc.perform(get("/api/user/following")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/following"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("muhu"));

        verify(userService, times(1)).getAllUserFollowingByEmail(userEmail, 0, 0);
    }

    @Test
    void getAllUserFollowingByEmailWhenThereIsNoUser() throws Exception {
        String userEmail = "test@example.com";

        Page<UserDto> followingPage = new PageImpl<>(List.of());

        when(userService.getAllUserFollowingByEmail(userEmail, 0, 0)).thenReturn(followingPage);

        mockMvc.perform(get("/api/user/following")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/user/following"));

        verify(userService, times(1)).getAllUserFollowingByEmail(userEmail, 0, 0);
    }

    @Test
    void loginApi() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto(
                "muhu", "password123"
        );

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                "muhu", "password123", authorities
        );

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);

        String secret = environment.getProperty("JWT_SECRET","jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4");

        Key secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String generatedJwt = Jwts.builder()
                .issuer("muhu")
                .subject("Jwt Token")
                .claim("username", "muhu")
                .claim("authorities",authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(".")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(new java.util.Date().getTime()+3000000))
                .signWith(secretKey)
                .compact();

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, generatedJwt));
    }

    @Test
    void loginApiWhenLoginFailed() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto(
                "wrongUsername", "wrongPass"
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Authentication failed"));

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Authentication failed"))
                .andExpect(jsonPath("$.path").value("/api/user/login"));
    }
}