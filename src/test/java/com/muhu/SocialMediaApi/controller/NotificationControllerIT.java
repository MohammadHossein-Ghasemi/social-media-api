package com.muhu.SocialMediaApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.model.NotificationDto;
import com.muhu.SocialMediaApi.model.NotificationRegistrationDto;
import com.muhu.SocialMediaApi.service.NotificationService;
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
class NotificationControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void saveNotification() throws Exception {
        User user = User.builder()
                .email("test@example.com")
                .username("muhu")
                .password("password1234")
                .id(14L)
                .build();
        NotificationRegistrationDto notificationRegistrationDto = NotificationRegistrationDto
                .builder()
                .message("Test Notification.")
                .user(user)
                .build();

        NotificationDto notificationDto = NotificationDto
                .builder()
                .id(14L)
                .message("Test Notification.")
                .build();

        when(notificationService.saveNotif(any(NotificationRegistrationDto.class))).thenReturn(notificationDto);

        mockMvc.perform(post("/api/notif/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .content(objectMapper.writeValueAsString(notificationRegistrationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/save"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification saved successfully."))
                .andExpect(jsonPath("$.data.message").value("Test Notification."));

        verify(notificationService, times(1)).saveNotif(any(NotificationRegistrationDto.class));
    }

    @Test
    void saveNotificationWhenSaveFailed() throws Exception {
        NotificationRegistrationDto notificationRegistrationDto = NotificationRegistrationDto
                .builder()
                .message("Test Notification.")
                .build();

        when(notificationService.saveNotif(any(NotificationRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/notif/save")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRegistrationDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/save"))
                .andExpect(content().string("There is problem on save notification"));

        verify(notificationService, times(1)).saveNotif(any(NotificationRegistrationDto.class));

    }

    @Test
    void deleteNotifById() throws Exception {
        Long notifId = 14L;

        when(notificationService.deleteNotifById(notifId)).thenReturn(true);

        mockMvc.perform(delete("/api/notif/delete/{notifId}", notifId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete"))
                .andExpect(content().string("The notification with ID 14 was deleted."));

        verify(notificationService, times(1)).deleteNotifById(notifId);
    }

    @Test
    void deleteNotifByIdWhenDeleteFailed() throws Exception {
        Long notifId = 14L;

        when(notificationService.deleteNotifById(notifId)).thenReturn(false);

        mockMvc.perform(delete("/api/notif/delete/{notifId}", notifId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete"))
                .andExpect(content().string("There is problem on deleting notification"));

        verify(notificationService, times(1)).deleteNotifById(notifId);
    }

    @Test
    void deleteNotifByUserId() throws Exception {
        Long userId = 14L;

        when(notificationService.deleteNotifByUserId(userId)).thenReturn(true);

        mockMvc.perform(delete("/api/notif/delete/user-id")
                        .param("userId", String.valueOf(userId))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete/user-id"))
                .andExpect(content().string("All notifications with user ID 14 were deleted."));

        verify(notificationService, times(1)).deleteNotifByUserId(userId);
    }

    @Test
    void deleteNotifByUserIdWhenDeleteFailed() throws Exception {
        Long userId = 14L;

        when(notificationService.deleteNotifByUserId(userId)).thenReturn(false);

        mockMvc.perform(delete("/api/notif/delete/user-id")
                        .param("userId", String.valueOf(userId))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete/user-id"))
                .andExpect(content().string("There is problem on deleting notification"));

        verify(notificationService, times(1)).deleteNotifByUserId(userId);
    }

    @Test
    void deleteNotifByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        when(notificationService.deleteNotifByUserEmail(userEmail)).thenReturn(true);

        mockMvc.perform(delete("/api/notif/delete/user-email")
                        .param("userEmail", userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete/user-email"))
                .andExpect(content().string("All notifications with user Email test@example.com were deleted."));

        verify(notificationService, times(1)).deleteNotifByUserEmail(userEmail);
    }

    @Test
    void deleteNotifByUserEmailWhenDeleteFailed() throws Exception {
        String userEmail = "wrong@example.com";

        when(notificationService.deleteNotifByUserEmail(userEmail)).thenReturn(false);

        mockMvc.perform(delete("/api/notif/delete/user-email")
                        .param("userEmail", userEmail)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/delete/user-email"))
                .andExpect(content().string("There is problem on deleting notification"));

        verify(notificationService, times(1)).deleteNotifByUserEmail(userEmail);
    }

    @Test
    void updateNotification() throws Exception {
        Long notifId = 14L;

        Notification requestNotification = Notification.builder()
                .message("Test Message.")
                .id(notifId)
                .build();

        NotificationDto notificationDto =NotificationDto.builder()
                .id(notifId)
                .message("New Message.")
                .build();

        when(notificationService.updateNotifById(eq(notifId), any(Notification.class))).thenReturn(notificationDto);

        mockMvc.perform(put("/api/notif/update/{notifId}", notifId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNotification)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/update"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification updated successfully."))
                .andExpect(jsonPath("$.data.message").value("New Message."));

        verify(notificationService, times(1)).updateNotifById(eq(notifId), any(Notification.class));
    }

    @Test
    void updateNotificationWhenUpdateFailed() throws Exception {
        Long notifId = 999L;

        Notification requestNotification = Notification.builder()
                .message("Test Message.")
                .id(notifId)
                .build();

        when(notificationService.updateNotifById(eq(notifId), any(Notification.class))).thenReturn(null);

        mockMvc.perform(put("/api/notif/update/{notifId}", notifId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNotification)))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/update"))
                .andExpect(content().string("There is problem on updating notification"));

        verify(notificationService, times(1)).updateNotifById(eq(notifId), any(Notification.class));

    }

    @Test
    void getNotifById() throws Exception {
        Long notifId = 14L;

        NotificationDto notificationDto =NotificationDto.builder()
                .id(notifId)
                .message("Test Message.")
                .build();

        when(notificationService.getNotifById(notifId)).thenReturn(notificationDto);

        mockMvc.perform(get("/api/notif/{notifId}", notifId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/14"))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification founded successfully."))
                .andExpect(jsonPath("$.data.message").value("Test Message."));

        verify(notificationService, times(1)).getNotifById(notifId);
    }

    @Test
    void getAllNotif() throws Exception {
        NotificationDto notificationDto =NotificationDto.builder()
                .id(14L)
                .message("Test Message.")
                .build();

        Page<NotificationDto> notifPage = new PageImpl<>(List.of(notificationDto));

        when(notificationService.getAllNotif(0, 0)).thenReturn(notifPage);

        mockMvc.perform(get("/api/notif/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/all"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].message").value("Test Message."));

        verify(notificationService, times(1)).getAllNotif(0,0);
    }

    @Test
    void getAllNotifWhenThereIsNoNotif() throws Exception {
        Page<NotificationDto> emptyPage = new PageImpl<>(List.of());

        when(notificationService.getAllNotif(0, 0)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/notif/all")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/all"));

        verify(notificationService, times(1)).getAllNotif(0,0);
    }

    @Test
    void getAllNotifByUserId() throws Exception {
        Long userId = 14L;

        NotificationDto notificationDto =NotificationDto.builder()
                .id(14L)
                .message("Test Message.")
                .build();

        Page<NotificationDto> notifPage = new PageImpl<>(List.of(notificationDto));

        when(notificationService.getAllNotifByUserId(userId,0,0)).thenReturn(notifPage);

        mockMvc.perform(get("/api/notif/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/user-id"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].message").value("Test Message."));

        verify(notificationService, times(1)).getAllNotifByUserId(userId,0,0);
    }

    @Test
    void getAllNotifByUserIdWhenThereIsNoNotif() throws Exception {
        Long userId = 999L;

        Page<NotificationDto> emptyPage = new PageImpl<>(List.of());

        when(notificationService.getAllNotifByUserId(userId,0,0)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/notif/user-id")
                        .param("userId", String.valueOf(userId))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/user-id"));

        verify(notificationService, times(1)).getAllNotifByUserId(userId,0,0);
    }

    @Test
    void getAllNotifByUserEmail() throws Exception {
        String userEmail = "test@example.com";

        NotificationDto notificationDto =NotificationDto.builder()
                .id(14L)
                .message("Test Message.")
                .build();

        Page<NotificationDto> notifPage = new PageImpl<>(List.of(notificationDto));

        when(notificationService.getAllNotifByUserEmail(userEmail,0,0)).thenReturn(notifPage);

        mockMvc.perform(get("/api/notif/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/user-email"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].message").value("Test Message."));

        verify(notificationService, times(1)).getAllNotifByUserEmail(userEmail,0,0);
    }

    @Test
    void getAllNotifByUserEmailWhenThereIsNoNotif() throws Exception {
        String userEmail = "test@example.com";

        Page<NotificationDto> emptyPage = new PageImpl<>(List.of());

        when(notificationService.getAllNotifByUserEmail(userEmail,0,0)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/notif/user-email")
                        .param("userEmail", userEmail)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(0))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/notif/user-email"));

        verify(notificationService, times(1)).getAllNotifByUserEmail(userEmail,0,0);
    }
}