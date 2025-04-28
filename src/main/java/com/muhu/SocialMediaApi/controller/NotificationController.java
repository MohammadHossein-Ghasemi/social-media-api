package com.muhu.SocialMediaApi.controller;

import com.muhu.SocialMediaApi.entity.Notification;
import com.muhu.SocialMediaApi.mapper.NotificationMapper;
import com.muhu.SocialMediaApi.model.NotificationDto;
import com.muhu.SocialMediaApi.model.NotificationRegistrationDto;
import com.muhu.SocialMediaApi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.muhu.SocialMediaApi.mapper.NotificationMapper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notif")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/save")
    public ResponseEntity<?> saveNotification(@RequestBody NotificationRegistrationDto notificationRDto){
        NotificationDto savedNotif = notificationToNotificationDto(
                notificationService.saveNotif(notificationRegistrationDtoToNotification(notificationRDto)));
        if (null == savedNotif){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/notif/save")
                    .body("There is problem on save notification");
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION,"/api/notif/save")
                .body(Map.of(
                        "status","success",
                        "message","Notification saved successfully.",
                        "data",savedNotif
                ));
    };

    @DeleteMapping("/delete/{notifId}")
    public ResponseEntity<?> deleteNotifById(@PathVariable Long notifId){
        Boolean isNotifDelete = notificationService.deleteNotifById(notifId);
        if (!isNotifDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/notif/delete")
                    .body("There is problem on deleting notification");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/notif/delete")
                .body("The notification with ID "+notifId+" was deleted.");
    }

    @DeleteMapping("/delete/user-id")
    public ResponseEntity<?> deleteNotifByUserId(@RequestParam Long userId){
        Boolean isNotifDelete = notificationService.deleteNotifByUserId(userId);
        if (!isNotifDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/notif/delete/user-id")
                    .body("There is problem on deleting notification");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/notif/delete/user-id")
                .body("All notifications with user ID "+userId+" were deleted.");
    }

    @DeleteMapping("/delete/user-email")
    public ResponseEntity<?> deleteNotifByUserEmail(@RequestParam String userEmail){
        Boolean isNotifDelete = notificationService.deleteNotifByUserEmail(userEmail);
        if (!isNotifDelete){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/notif/delete/user-email")
                    .body("There is problem on deleting notification");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/notif/delete/user-email")
                .body("All notifications with user Email "+ userEmail +" were deleted.");
    }

    @PutMapping("/update/{notifId}")
    public ResponseEntity<?> updateNotification(@PathVariable Long notifId,
                                                @RequestBody Notification notification){
        NotificationDto updatedNotif = notificationToNotificationDto(notificationService.updateNotifById(notifId,notification)) ;
        if (null == updatedNotif){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.LOCATION,"/api/notif/update")
                    .body("There is problem on updating notification");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/notif/update")
                .body(Map.of(
                        "status","success",
                        "message","Notification updated successfully.",
                        "data",updatedNotif
                ));

    }

    @GetMapping("/{notifId}")
    public ResponseEntity<?> getNotifById(@PathVariable Long notifId){
        NotificationDto foundedNotif = notificationToNotificationDto(notificationService.getNotifById(notifId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION,"/api/notif/"+notifId)
                .body(Map.of(
                        "status","success",
                        "message","Notification founded successfully.",
                        "data",foundedNotif
                ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNotif(){
        List<NotificationDto> allPost = notificationService.getAllNotif()
                .stream()
                .map(NotificationMapper::notificationToNotificationDto)
                .toList();

        HttpStatus httpStatus = allPost.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/notif/all")
                .body(allPost);
    }

    @GetMapping("/user-id")
    public ResponseEntity<?> getAllNotifByUserId(@RequestParam Long userId){
        List<NotificationDto> allNotifByUserId = notificationService.getAllNotifByUserId(userId)
                .stream()
                .map(NotificationMapper::notificationToNotificationDto)
                .toList();;

        HttpStatus httpStatus = allNotifByUserId.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/notif/user-id")
                .body(allNotifByUserId);
    }

    @GetMapping("/user-email")
    public ResponseEntity<?> getAllNotifByUserEmail(@RequestParam String userEmail){
        List<NotificationDto> allNotifByUserEmail = notificationService.getAllNotifByUserEmail(userEmail)
                .stream()
                .map(NotificationMapper::notificationToNotificationDto)
                .toList();;

        HttpStatus httpStatus = allNotifByUserEmail.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity
                .status(httpStatus)
                .header(HttpHeaders.LOCATION,"/api/notif/user-email")
                .body(allNotifByUserEmail);
    }
}
