package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user ")
    @Override
    Page<Notification> findAll (@NonNull Pageable pageable);

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.id = :id")
    @Override
    Optional<Notification> findById (@NonNull @Param("id") Long id);

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.user.id = :user_id")
    Page<Notification> findByUserId (@NonNull @Param("user_id") Long userId,Pageable pageable);

    @Query("SELECT n FROM notification n " +
            "LEFT JOIN FETCH n.user "+
            "WHERE n.user.email = :user_email")
    Page<Notification> findByUserEmail (@NonNull @Param("user_email") String userEmail,Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM notification n " +
            "WHERE n.user.id = :user_id")
    void deleteByUserId(@NonNull @Param("user_id") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM notification n " +
            "WHERE n.user.email = :user_email")
    void deleteByUserEmail(@NonNull @Param("user_email") String userEmail);
}
