package com.muhu.SocialMediaApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhu.SocialMediaApi.entity.Comment;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.CommentDto;
import com.muhu.SocialMediaApi.model.CommentRegistrationDto;
import com.muhu.SocialMediaApi.model.UserSummaryDto;
import com.muhu.SocialMediaApi.service.CommentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    void saveComment() throws Exception {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L)
                .user(user)
                .content("Test Content.")
                .build();
        CommentRegistrationDto commentRegistrationDto = CommentRegistrationDto.builder()
                .user(user)
                .post(post)
                .content("Test Content.")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .user(UserSummaryDto.builder().id(user.getId()).build())
                .postId(post.getId())
                .content("Test Content.")
                .build();

        when(commentService.saveComment(any(CommentRegistrationDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/api/comment/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRegistrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Comment saved successfully."))
                .andExpect(jsonPath("$.data.id").value(14L))
                .andExpect(jsonPath("$.data.user.id").value(14L))
                .andExpect(jsonPath("$.data.postId").value(14L))
                .andExpect(jsonPath("$.data.content").value("Test Content."));

        verify(commentService, times(1)).saveComment(any(CommentRegistrationDto.class));
    }

    @Test
    void saveCommentWhenSaveFailed() throws Exception {
        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L)
                .user(user)
                .content("Test Content.")
                .build();
        CommentRegistrationDto commentRegistrationDto = CommentRegistrationDto.builder()
                .user(user)
                .post(post)
                .content("Test Content.")
                .build();

        when(commentService.saveComment(any(CommentRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/comment/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRegistrationDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/save"))
                .andExpect(content().string("There is problem on save comment"));

        verify(commentService, times(1)).saveComment(any(CommentRegistrationDto.class));
    }

    @Test
    void deleteCommentById() throws Exception {
        Long commentId = 14L;

        when(commentService.deleteCommentById(commentId)).thenReturn(true);

        mockMvc.perform(delete("/api/comment/delete/{commentId}", commentId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/api/comment/delete/14"))
                .andExpect(content().string("The comment with ID 14 was deleted."));

        verify(commentService, times(1)).deleteCommentById(commentId);
    }

    @Test
    void deleteCommentByIdWhenDeleteFailed() throws Exception {
        Long commentId = 14L;

        when(commentService.deleteCommentById(commentId)).thenReturn(false);

        mockMvc.perform(delete("/api/comment/delete/{commentId}", commentId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", "/api/comment/delete/14"))
                .andExpect(content().string("There is problem on deleting comment."));

        verify(commentService, times(1)).deleteCommentById(commentId);
    }

    @Test
    void deleteCommentByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        when(commentService.deleteCommentByUserEmail(userEmail)).thenReturn(true);

        mockMvc.perform(delete("/api/comment/delete/user-email")
                        .param("userEmail", userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/api/comment/delete/user-email"))
                .andExpect(content().string("All comments with user Email test@example.com were deleted."));

        verify(commentService, times(1)).deleteCommentByUserEmail(userEmail);
    }

    @Test
    void deleteCommentByUserEmailWhenDeleteFailed() throws Exception {
        String userEmail = "test@example.com";

        when(commentService.deleteCommentByUserEmail(userEmail)).thenReturn(false);

        mockMvc.perform(delete("/api/comment/delete/user-email")
                        .param("userEmail", userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", "/api/comment/delete/user-email"))
                .andExpect(content().string("There is problem on deleting comment."));

        verify(commentService, times(1)).deleteCommentByUserEmail(userEmail);
    }

    @Test
    void deleteCommentByPostId() throws Exception {
        Long postId = 14L;

        when(commentService.deleteCommentByPostId(postId)).thenReturn(true);

        mockMvc.perform(delete("/api/comment/delete/post-id")
                        .param("postId", String.valueOf(postId))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/api/comment/delete/post-id"))
                .andExpect(content().string("All comments with post ID 14 were deleted."));

        verify(commentService, times(1)).deleteCommentByPostId(postId);
    }

    @Test
    void deleteCommentByPostIdWhenDeleteFailed() throws Exception {
        Long postId = 14L;

        when(commentService.deleteCommentByPostId(postId)).thenReturn(false);

        mockMvc.perform(delete("/api/comment/delete/post-id")
                        .param("postId", String.valueOf(postId))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", "/api/comment/delete/post-id"))
                .andExpect(content().string("There is problem on deleting comment"));

        verify(commentService, times(1)).deleteCommentByPostId(postId);
    }

    @Test
    void updateComment() throws Exception {
        Long commentId = 14L;

        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();
        Post post = Post.builder()
                .id(14L)
                .user(user)
                .content("Test Content.")
                .build();

        Comment comment = Comment.builder()
                .content("Updated Content.")
                .user(user)
                .post(post)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(commentId)
                .content("Updated Content.")
                .user(UserSummaryDto.builder().id(user.getId()).build())
                .postId(post.getId())
                .build();

        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(commentDto);

        mockMvc.perform(put("/api/comment/update/{commentId}", commentId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/update/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Comment updated successfully."))
                .andExpect(jsonPath("$.data.id").value(14))
                .andExpect(jsonPath("$.data.user.id").value(14))
                .andExpect(jsonPath("$.data.postId").value(14))
                .andExpect(jsonPath("$.data.content").value("Updated Content."));

        verify(commentService, times(1)).updateComment(eq(commentId), any(Comment.class));
    }

    @Test
    void updateCommentWhenUpdateFailed() throws Exception {
        Long commentId = 14L;

        User user = User.builder()
                .id(14L)
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .build();

        Post post = Post.builder()
                .id(14L)
                .user(user)
                .content("Test Content.")
                .build();

        Comment comment = Comment.builder()
                .content("Updated Content.")
                .user(user)
                .post(post)
                .build();

        when(commentService.updateComment(eq(commentId), any(Comment.class))).thenReturn(null);

        mockMvc.perform(put("/api/comment/update/{commentId}", commentId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/update/14"))
                .andExpect(content().string("There is problem on updating comment"));

        verify(commentService, times(1)).updateComment(eq(commentId), any(Comment.class));
    }

    @Test
    void getAllComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .content("Test Content.")
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .build();

        Page<CommentDto> commentPage = new PageImpl<>(List.of(commentDto));

        when(commentService.getAllComment(0, 0)).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/all"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(14L));

        verify(commentService, times(1)).getAllComment(0,0);
    }

    @Test
    void getAllCommentWhenThereIsNoContent() throws Exception {
        Page<CommentDto> commentPage = new PageImpl<>(List.of());

        when(commentService.getAllComment(0, 0)).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/all"));

        verify(commentService, times(1)).getAllComment(0,0);
    }

    @Test
    void getCommentById() throws Exception {
        Long commentId = 14L;

        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .content("Test Content.")
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .build();

        when(commentService.getCommentById(eq(commentId))).thenReturn(commentDto);

        mockMvc.perform(get("/api/comment/{commentId}", commentId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Comment founded successfully."))
                .andExpect(jsonPath("$.data.id").value(14L))
                .andExpect(jsonPath("$.data.user.id").value(14L))
                .andExpect(jsonPath("$.data.postId").value(14L))
                .andExpect(jsonPath("$.data.content").value("Test Content."));

        verify(commentService, times(1)).getCommentById(eq(commentId));
    }

    @Test
    void getAllCommentByUserId() throws Exception {
        Long userId = 14L;

        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .content("Test Content.")
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .build();

        Page<CommentDto> commentPage = new PageImpl<>(List.of(commentDto));

        when(commentService.getAllCommentByUserId(eq(userId),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/user-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(14L))
                .andExpect(jsonPath("$.content[0].user.id").value(14L));

        verify(commentService, times(1)).getAllCommentByUserId(eq(userId),eq(0),eq(0));
    }

    @Test
    void getAllCommentByUserIdWhenThereIsNoComment() throws Exception {
        Long userId = 14L;

        Page<CommentDto> commentPage = new PageImpl<>(List.of());

        when(commentService.getAllCommentByUserId(eq(userId),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/user-id"));

        verify(commentService, times(1)).getAllCommentByUserId(eq(userId),eq(0),eq(0));
    }

    @Test
    void getAllCommentByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .content("Test Content.")
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .build();

        Page<CommentDto> commentPage = new PageImpl<>(List.of(commentDto));

        when(commentService.getAllCommentByUserEmail(eq(userEmail),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/user-email"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(14L))
                .andExpect(jsonPath("$.content[0].user.id").value(14L));

        verify(commentService, times(1)).getAllCommentByUserEmail(eq(userEmail),eq(0),eq(0));
    }

    @Test
    void getAllCommentByUserEmailWhenThereIsNoComment() throws Exception {
        String userEmail = "test@example.com";


        Page<CommentDto> commentPage = new PageImpl<>(List.of());

        when(commentService.getAllCommentByUserEmail(eq(userEmail),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/user-email"));

        verify(commentService, times(1)).getAllCommentByUserEmail(eq(userEmail),eq(0),eq(0));
    }

    @Test
    void getAllCommentByPostId() throws Exception {
        Long postId = 14L;

        CommentDto commentDto = CommentDto.builder()
                .id(14L)
                .content("Test Content.")
                .user(UserSummaryDto.builder().id(14L).build())
                .postId(14L)
                .build();

        Page<CommentDto> commentPage = new PageImpl<>(List.of(commentDto));

        when(commentService.getAllCommentByPostId(eq(postId),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/post-id")
                        .param("postId", String.valueOf(postId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/post-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(14L))
                .andExpect(jsonPath("$.content[0].user.id").value(14L));

        verify(commentService, times(1)).getAllCommentByPostId(eq(postId),eq(0),eq(0));
    }

    @Test
    void getAllCommentByPostIdWhenThereIsNoComment() throws Exception {
        Long postId = 14L;

        Page<CommentDto> commentPage = new PageImpl<>(List.of());

        when(commentService.getAllCommentByPostId(eq(postId),eq(0),eq(0))).thenReturn(commentPage);

        mockMvc.perform(get("/api/comment/post-id")
                        .param("postId", String.valueOf(postId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/comment/post-id"));

        verify(commentService, times(1)).getAllCommentByPostId(eq(postId),eq(0),eq(0));
    }
}