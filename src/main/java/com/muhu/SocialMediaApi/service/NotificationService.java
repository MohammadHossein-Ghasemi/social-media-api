package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface NotificationService {
    Notification saveNotif(Notification notification) throws BadRequestException;
    Boolean deleteNotifById(Long notifId);
    Boolean deleteNotifByUserId(Long userId);
    Boolean deleteNotifByUserEmail(String userEmail);
    Notification updateNotifById(Long notifId,Notification notification);
    Notification getNotifById(Long notifId);
    List<Notification> getAllNotif();
    List<Notification> getAllNotifByUserId(Long userId);
    List<Notification> getAllNotifByUserEmail(String userEmail);
}
