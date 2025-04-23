package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.repository.NotificationRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public Notification saveNotif(Notification notification) throws BadRequestException {
        if(!userValidation(notification.getUser())){
            return null;
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Boolean deleteNotifById(Long notifId) {
        if (!notificationRepository.existsById(notifId)){
            throw new ResourceNotFoundException("There is no post with ID : " + notifId);
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

    private boolean userValidation(User user)  {
        if (user == null || user.getId() == null || user.getEmail() == null) {
            try {
                throw new BadRequestException("The use can not be null.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Long userId = user.getId();
        String userEmail = user.getEmail();

        if (!userRepository.existsByEmail(userEmail)) {
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        } else if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("There is no user with ID : " + userId);
        }

        return true;
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
