package com.muhu.SocialMediaApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhu.SocialMediaApi.entity.Post;
import com.muhu.SocialMediaApi.model.PostDto;
import com.muhu.SocialMediaApi.model.PostRegistrationDto;
import com.muhu.SocialMediaApi.service.PostService;
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
class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    void savePost() throws Exception {
        PostRegistrationDto postRegistrationDto = new PostRegistrationDto();
        postRegistrationDto.setContent("Test Content.");

        PostDto postDto = new PostDto();
        postDto.setId(14L);
        postDto.setContent("Test Content.");

        when(postService.savePost(any(PostRegistrationDto.class))).thenReturn(postDto);

        mockMvc.perform(post("/api/post/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .content(objectMapper.writeValueAsString(postRegistrationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Post saved successfully."))
                .andExpect(jsonPath("$.data.content").value("Test Content."));

        verify(postService, times(1)).savePost(any(PostRegistrationDto.class));
    }

    @Test
    void savePostWhenSaveFailed() throws Exception {
        PostRegistrationDto postRegistrationDto = new PostRegistrationDto();
        postRegistrationDto.setContent("Test Content.");

        when(postService.savePost(any(PostRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/post/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .content(objectMapper.writeValueAsString(postRegistrationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/save"))
                .andExpect(content().string("There is problem on save post"));

        verify(postService, times(1)).savePost(any(PostRegistrationDto.class));
    }

    @Test
    void deletePostById() throws Exception {
        Long postId = 14L;

        when(postService.deletePostById(postId)).thenReturn(true);

        mockMvc.perform(delete("/api/post/delete/{postId}", postId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/delete"))
                .andExpect(content().string("The post with ID 14 was deleted."));

        verify(postService, times(1)).deletePostById(postId);
    }

    @Test
    void updatePostById() throws Exception {
        Long postId = 14L;

        Post post = new Post();
        post.setContent("Test Content");

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setContent("Updated Content.");

        when(postService.updatePostById(any(Long.class), any(Post.class))).thenReturn(postDto);

        mockMvc.perform(put("/api/post/update/{postId}", postId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/update"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Post updated successfully."))
                .andExpect(jsonPath("$.data.content").value("Updated Content."));

        verify(postService, times(1)).updatePostById(any(Long.class), any(Post.class));
    }

    @Test
    void updatePostByIdWhenThereIsProblemOnUpdating() throws Exception {
        Long postId = 14L;

        Post post = new Post();
        post.setContent("Test Content.");

        when(postService.updatePostById(any(Long.class), any(Post.class))).thenReturn(null);

        mockMvc.perform(put("/api/post/update/{postId}", postId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/update"))
                .andExpect(content().string("There is problem on updating post"));

        verify(postService, times(1)).updatePostById(any(Long.class), any(Post.class));
    }

    @Test
    void getPostById() throws Exception {
        Long postId = 14L;

        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setContent("Test Content.");

        when(postService.getPostById(postId)).thenReturn(postDto);

        mockMvc.perform(get("/api/post/{postId}", postId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Post founded successfully."))
                .andExpect(jsonPath("$.data.content").value("Test Content."));

        verify(postService, times(1)).getPostById(postId);
    }

    @Test
    void getAllPost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setId(14L);
        postDto.setContent("Test Content.");

        Page<PostDto> postPage = new PageImpl<>(List.of(postDto));

        when(postService.getAllPost(0, 0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/all"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test Content."));

        verify(postService, times(1)).getAllPost(0,0);
    }

    @Test
    void getAllPostWhenThereIsNoPost() throws Exception {
        Page<PostDto> postPage = new PageImpl<>(List.of());

        when(postService.getAllPost(0, 0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/all"));

        verify(postService, times(1)).getAllPost(0,0);
    }

    @Test
    void getAllPostByUserId() throws Exception {
        Long userId = 14L;

        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("Test Content.");

        Page<PostDto> postPage = new PageImpl<>(List.of(postDto));

        when(postService.getAllPostByUserId(userId,0,0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/user-id")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/user-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test Content."));

        verify(postService, times(1)).getAllPostByUserId(userId,0,0);
    }

    @Test
    void getAllPostByUserIdWhenThereIsNoPost() throws Exception {
        Long userId = 14L;

        Page<PostDto> postPage = new PageImpl<>(List.of());

        when(postService.getAllPostByUserId(userId,0,0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/user-id")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/user-id"));

        verify(postService, times(1)).getAllPostByUserId(userId,0,0);
    }

    @Test
    void getAllPostByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("Test Content.");

        Page<PostDto> postPage = new PageImpl<>(List.of(postDto));

        when(postService.getAllPostByUserEmail(userEmail,0,0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/user-email")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", String.valueOf(userEmail))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/user-email"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test Content."));

        verify(postService, times(1)).getAllPostByUserEmail(userEmail,0,0);
    }

    @Test
    void getAllPostByUserEmailWhenThereIsNoPost() throws Exception {
        String userEmail = "test@example.com";

        Page<PostDto> postPage = new PageImpl<>(List.of());

        when(postService.getAllPostByUserEmail(userEmail,0,0)).thenReturn(postPage);

        mockMvc.perform(get("/api/post/user-email")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .param("userEmail", String.valueOf(userEmail))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/post/user-email"));

        verify(postService, times(1)).getAllPostByUserEmail(userEmail,0,0);
    }
}