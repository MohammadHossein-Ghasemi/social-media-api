package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like , Long> {

    @Query("SELECT l FROM like l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH l.post ")
    @Override
    Page<Like> findAll (@NonNull Pageable pageable);

    @Query("SELECT l FROM like l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH l.post " +
            "WHERE l.id = :id")
    @Override
    Optional<Like> findById (@NonNull @Param("id") Long id);

    @Query("SELECT l FROM like l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH l.post " +
            "WHERE l.user.id = :user_id")
    Page<Like> findByUserId (@NonNull @Param("user_id") Long userId,Pageable pageable);

    @Query("SELECT l FROM like l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH l.post " +
            "WHERE l.user.email = :user_email")
    Page<Like> findByUserEmail (@NonNull @Param("user_email") String userEmail,Pageable pageable);

    @Query("SELECT l FROM like l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH l.post " +
            "WHERE l.post.id = :post_id")
    Page<Like> findByPostId (@NonNull @Param("post_id") Long postId,Pageable pageable);
}
