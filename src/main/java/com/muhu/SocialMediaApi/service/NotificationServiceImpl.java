package com.muhu.SocialMediaApi.service;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.entity.User;
import com.muhu.SocialMediaApi.exception.ResourceNotFoundException;
import com.muhu.SocialMediaApi.mapper.NotificationMapper;
import com.muhu.SocialMediaApi.model.NotificationDto;
import com.muhu.SocialMediaApi.model.NotificationRegistrationDto;
import com.muhu.SocialMediaApi.repository.NotificationRepository;
import com.muhu.SocialMediaApi.repository.UserRepository;
import com.muhu.SocialMediaApi.service.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

import static com.muhu.SocialMediaApi.mapper.NotificationMapper.notificationRegistrationDtoToNotification;
import static com.muhu.SocialMediaApi.mapper.NotificationMapper.notificationToNotificationDto;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public NotificationDto saveNotif(NotificationRegistrationDto notificationRegistrationDto){
        User userValid = validation.isUserValid(notificationRegistrationDto.getUser());
        if(null == userValid){
            return null;
        }
        notificationRegistrationDto.setUser(userValid);
        return notificationToNotificationDto(notificationRepository.save(
                notificationRegistrationDtoToNotification(notificationRegistrationDto)));
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
    public NotificationDto updateNotifById(Long notifId, Notification notification) {
        AtomicReference<Notification> updatedNotif = new AtomicReference<>();
        notificationRepository.findById(notifId).ifPresentOrElse(
                foundedNotif->{
                    updateNonNullFields(foundedNotif,notification);
                    updatedNotif.set(notificationRepository.save(foundedNotif));
                },
                ()-> {throw new ResourceNotFoundException("There is no post with ID : "+ notifId);}
        );
        return notificationToNotificationDto(updatedNotif.get());
    }

    @Override
    public NotificationDto getNotifById(Long notifId) {
        return notificationToNotificationDto(notificationRepository.findById(notifId)
                .orElseThrow(()-> new ResourceNotFoundException("There is no notification with ID : "+notifId)));
    }

    @Override
    public Page<NotificationDto> getAllNotif(Integer page, Integer size) {

        return notificationRepository.findAll(validation.pageAndSizeValidation(page, size))
                .map(NotificationMapper::notificationToNotificationDto);
    }

    @Override
    public Page<NotificationDto> getAllNotifByUserId(Long userId,Integer page, Integer size) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("There is no user with ID : "+userId);
        }
        return notificationRepository.findByUserId(userId,validation.pageAndSizeValidation(page, size))
                .map(NotificationMapper::notificationToNotificationDto);
    }

    @Override
    public Page<NotificationDto> getAllNotifByUserEmail(String userEmail,Integer page, Integer size) {
        if (!userRepository.existsByEmail(userEmail)){
            throw new ResourceNotFoundException("There is no user with Email : " + userEmail);
        }
        return notificationRepository.findByUserEmail(userEmail,validation.pageAndSizeValidation(page, size))
                .map(NotificationMapper::notificationToNotificationDto);
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
