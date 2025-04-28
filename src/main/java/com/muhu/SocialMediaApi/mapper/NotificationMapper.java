package com.muhu.SocialMediaApi.mapper;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.model.NotificationDto;
import com.muhu.SocialMediaApi.model.NotificationRegistrationDto;

public class NotificationMapper {
    private NotificationMapper(){}

    public static Notification notificationRegistrationDtoToNotification(NotificationRegistrationDto notificationRegistrationDto){
        return Notification.builder()
                .user(notificationRegistrationDto.getUser())
                .message(notificationRegistrationDto.getMessage())
                .isRead(notificationRegistrationDto.getIsRead())
                .build();
    }

    public static NotificationDto notificationToNotificationDto(Notification notification){
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .user(UserMapper.userToUserSummeryDto(notification.getUser()))
                .build();
    }
}
