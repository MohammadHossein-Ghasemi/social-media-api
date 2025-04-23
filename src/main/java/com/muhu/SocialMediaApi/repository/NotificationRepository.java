package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user ")
    @Override
    List<Notification> findAll ();

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.id = :id")
    @Override
    Optional<Notification> findById (@NonNull @Param("id") Long id);

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.user.id = :user_id")
    List<Notification> findByUserId (@NonNull @Param("user_id") Long userId);

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.user.email = :user_email")
    List<Notification> findByUserEmail (@NonNull @Param("user_email") String userEmail);

    @Query("DELETE FROM notification n " +
            "WHERE n.user.id = :user_id")
    void deleteByUserId(@NonNull @Param("user_id") Long userId);

    @Query("DELETE FROM notification n " +
            "WHERE n.user.email = :user_email")
    void deleteByUserEmail(@NonNull @Param("user_email") String userEmail);
}
