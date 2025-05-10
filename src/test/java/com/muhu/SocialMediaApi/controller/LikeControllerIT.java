package com.muhu.SocialMediaApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhu.SocialMediaApi.entity.Like;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.LikeDto;
import com.muhu.SocialMediaApi.model.LikeRegistrationDto;
import com.muhu.SocialMediaApi.model.UserSummaryDto;
import com.muhu.SocialMediaApi.service.LikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LikeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LikeService likeService;

    @Test
    void saveLike() throws Exception {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L).user(user)
                .content("Test Content.")
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto
                .builder()
                .post(post)
                .user(user)
                .build();

        LikeDto likeDto = LikeDto.builder()
                .user(UserSummaryDto.builder().id(user.getId()).build())
                .postId(post.getId())
                .id(14L)
                .build();

        when(likeService.saveLike(any(LikeRegistrationDto.class))).thenReturn(likeDto);

        mockMvc.perform(post("/api/like/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRegistrationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/save"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Like saved successfully."))
                .andExpect(jsonPath("$.data.user.id").value(14L))
                .andExpect(jsonPath("$.data.postId").value(14L));

        verify(likeService, times(1)).saveLike(any(LikeRegistrationDto.class));
    }

    @Test
    void saveLikeWheSaveFailed() throws Exception {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L).user(user)
                .content("Test Content.")
                .build();
        LikeRegistrationDto likeRegistrationDto = LikeRegistrationDto
                .builder()
                .post(post)
                .user(user)
                .build();

        when(likeService.saveLike(any(LikeRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/like/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRegistrationDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/save"))
                .andExpect(content().string("There is problem on save like"));

        verify(likeService, times(1)).saveLike(any(LikeRegistrationDto.class));
    }

    @Test
    void deleteLikeById() throws Exception {
        Long likeId = 14L;

        when(likeService.deleteLikeById(likeId)).thenReturn(true);

        mockMvc.perform(delete("/api/like/delete/{likeId}", likeId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/delete/14"))
                .andExpect(content().string("The like with ID 14 was deleted."));

        verify(likeService, times(1)).deleteLikeById(likeId);
    }

    @Test
    void deleteLikeByIdWhenDeleteFailed() throws Exception {
        Long likeId = 14L;

        when(likeService.deleteLikeById(likeId)).thenReturn(false);

        mockMvc.perform(delete("/api/like/delete/{likeId}", likeId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/delete/14"))
                .andExpect(content().string("There is problem on deleting like"));

        verify(likeService, times(1)).deleteLikeById(likeId);
    }

    @Test
    void updateLike() throws Exception {
        Long likeId = 14L;

        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L).user(user)
                .content("Test Content.")
                .build();

        Like like = Like.builder()
                .post(post)
                .user(user)
                .id(likeId)
                .build();

        LikeDto likeDto = LikeDto.builder()
                .user(UserSummaryDto.builder().id(user.getId()).build())
                .postId(post.getId())
                .id(14L)
                .build();

        when(likeService.updateLike(eq(likeId),any(Like.class))).thenReturn(likeDto);

        mockMvc.perform(put("/api/like/update/{likeId}", likeId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/update/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Like updated successfully."))
                .andExpect(jsonPath("$.data.user.id").value(14L))
                .andExpect(jsonPath("$.data.postId").value(14L));

        verify(likeService, times(1)).updateLike(eq(likeId),any(Like.class));
    }

    @Test
    void updateLikeWhenUpdateFailed() throws Exception {
        Long likeId = 14L;

        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L).user(user)
                .content("Test Content.")
                .build();

        Like like = Like.builder()
                .post(post)
                .user(user)
                .id(likeId)
                .build();

        when(likeService.updateLike(eq(likeId), any(Like.class))).thenReturn(null);

        mockMvc.perform(put("/api/like/update/{likeId}", likeId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/update/14"))
                .andExpect(content().string("There is problem on updating like"));

        verify(likeService, times(1)).updateLike(eq(likeId),any(Like.class));
    }

    @Test
    void getLikeByID() throws Exception {
        Long likeId = 14L;

        LikeDto likeDto = LikeDto.builder()
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .id(14L)
                .build();

        when(likeService.getLikeByID(likeId)).thenReturn(likeDto);

        mockMvc.perform(get("/api/like/{likeId}", likeId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Like founded successfully."))
                .andExpect(jsonPath("$.data.id").value(14L))
                .andExpect(jsonPath("$.data.user.id").value(14L))
                .andExpect(jsonPath("$.data.postId").value(14L));

        verify(likeService, times(1)).getLikeByID(eq(likeId));
    }

    @Test
    void getAllLike() throws Exception {
        LikeDto like = LikeDto.builder()
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .id(14L)
                .build();

        Page<LikeDto> likePage = new PageImpl<>(List.of(like));

        when(likeService.getAllLike(0, 0)).thenReturn(likePage);

        mockMvc.perform(get("/api/like/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/all"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(14));

        verify(likeService, times(1)).getAllLike(0,0);
    }

    @Test
    void getAllLikeWhenThereIsNoLike() throws Exception {
        Page<LikeDto> likePage = new PageImpl<>(List.of());

        when(likeService.getAllLike(0, 0)).thenReturn(likePage);

        mockMvc.perform(get("/api/like/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/all"));

        verify(likeService, times(1)).getAllLike(0,0);
    }

    @Test
    void getAllLikeByUserId() throws Exception {
        Long userId = 14L;

        LikeDto like = LikeDto.builder()
                .user(UserSummaryDto.builder().id(userId).build())
                .postId(14L)
                .id(14L)
                .build();

        Page<LikeDto> likePage = new PageImpl<>(List.of(like));

        when(likeService.getAllLikeByUserId(eq(userId), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/user-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].user.id").value(14L));

        verify(likeService, times(1)).getAllLikeByUserId(eq(userId), eq(0), eq(0));
    }

    @Test
    void getAllLikeByUserIdWhenThereIsNoLike() throws Exception {
        Long userId = 14L;

        Page<LikeDto> likePage = new PageImpl<>(List.of());

        when(likeService.getAllLikeByUserId(eq(userId), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/user-id"));

        verify(likeService, times(1)).getAllLikeByUserId(eq(userId), eq(0), eq(0));
    }

    @Test
    void getAllLikeByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        LikeDto like = LikeDto.builder()
                .user(UserSummaryDto.builder().email(userEmail).build())
                .postId(14L)
                .id(14L)
                .build();

        Page<LikeDto> likePage = new PageImpl<>(List.of(like));

        when(likeService.getAllLikeByUserEmail(eq(userEmail), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/user-email"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].user.email").value("test@example.com"));

        verify(likeService, times(1)).getAllLikeByUserEmail(eq(userEmail), eq(0), eq(0));
    }

    @Test
    void getAllLikeByUserEmailWhenThereIsNoLike() throws Exception {
        String userEmail = "test@example.com";

        Page<LikeDto> likePage = new PageImpl<>(List.of());

        when(likeService.getAllLikeByUserEmail(eq(userEmail), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/user-email"));

        verify(likeService, times(1)).getAllLikeByUserEmail(eq(userEmail), eq(0), eq(0));
    }

    @Test
    void getAllLikeByPostId() throws Exception {
        Long postId = 14L;

        LikeDto like = LikeDto.builder()
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(postId)
                .id(14L)
                .build();

        Page<LikeDto> likePage = new PageImpl<>(List.of(like));

        when(likeService.getAllLikeByPostId(eq(postId), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/post-id")
                        .param("postId", String.valueOf(postId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/post-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].postId").value(14L));

        verify(likeService, times(1)).getAllLikeByPostId(eq(postId), eq(0), eq(0));
    }

    @Test
    void getAllLikeByPostIdWhenThereIsNoLike() throws Exception {
        Long postId = 14L;

        Page<LikeDto> likePage = new PageImpl<>(List.of());

        when(likeService.getAllLikeByPostId(eq(postId), eq(0), eq(0))).thenReturn(likePage);

        mockMvc.perform(get("/api/like/post-id")
                        .param("postId", String.valueOf(postId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/like/post-id"));

        verify(likeService, times(1)).getAllLikeByPostId(eq(postId), eq(0), eq(0));
    }
}