package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.model.NotificationDto;
import com.muhu.SocialMediaApi.model.NotificationRegistrationDto;
import org.springframework.data.domain.Page;

public interface NotificationService {
    NotificationDto saveNotif(NotificationRegistrationDto notificationRegistrationDto);
    Boolean deleteNotifById(Long notifId);
    Boolean deleteNotifByUserId(Long userId);
    Boolean deleteNotifByUserEmail(String userEmail);
    NotificationDto updateNotifById(Long notifId,Notification notification);
    NotificationDto getNotifById(Long notifId);
    Page<NotificationDto> getAllNotif(Integer page, Integer size);
    Page<NotificationDto> getAllNotifByUserId(Long userId,Integer page,Integer size);
    Page<NotificationDto> getAllNotifByUserEmail(String userEmail,Integer page,Integer size);
}
