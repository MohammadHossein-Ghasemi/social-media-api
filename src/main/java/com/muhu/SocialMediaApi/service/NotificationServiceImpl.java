package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.NotificationRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final Validation Validation;

    @Override
    public Notification saveNotif(Notification notification){
        if(!Validation.isUserValid(notification.getUser())){
            return null;
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Boolean deleteNotifById(Long notifId) {
        if (!notificationRepository.existsById(notifId)){
            throw new ResourceNotFoundException("There is no notification with ID : " + notifId);
        }
        notificationRepository.deleteById(notifId);
        return true;
    }

    @Override
    public Boolean deleteNotifByUserId(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        notificationRepository.deleteByUserId(userId);
        return true;
    }

    @Override
    public Boolean deleteNotifByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : "+userEmail);
        }
        notificationRepository.deleteByUserEmail(userEmail);
        return true;
    }

    @Override
    public Notification updateNotifById(Long notifId, Notification notification) {
        AtomicReference<Notification> updatedNotif = new AtomicReference<>();
        notificationRepository.findById(notifId).ifPresentOrElse(
                foundedNotif->{
                    updateNonNullFields(foundedNotif,notification);
                    updatedNotif.set(notificationRepository.save(foundedNotif));
                },
                ()-> {throw new ResourceNotFoundException("There is no post with ID : "+ notifId);}
        );
        return updatedNotif.get();
    }

    @Override
    public Notification getNotifById(Long notifId) {
        return notificationRepository.findById(notifId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no notification with ID : "+notifId));
    }

    @Override
    public List<Notification> getAllNotif() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getAllNotifByUserId(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getAllNotifByUserEmail(String userEmail) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        }
        return notificationRepository.findByUserEmail(userEmail);
    }

    private void updateNonNullFields(Notification updatedNotif , Notification inputNotif){
        if (inputNotif.getId() != null) updatedNotif.setId(inputNotif.getId());
        if (inputNotif.getUser() != null) {

            Long userId = inputNotif.getUser().getId();
            String userEmail = inputNotif.getUser().getEmail();

            if (!userRepository.existsByEmail(userEmail)) {
                throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
            } else if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException("There is no user with ID : " + userId);
            }

            updatedNotif.setUser(inputNotif.getUser());
        }
        if (inputNotif.getMessage() != null) updatedNotif.setMessage(inputNotif.getMessage());
        if (inputNotif.getIsRead() != null) updatedNotif.setIsRead(inputNotif.getIsRead());
    }
}
