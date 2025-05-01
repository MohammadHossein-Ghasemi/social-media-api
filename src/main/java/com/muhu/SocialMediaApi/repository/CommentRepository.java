package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment , Long> {

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post ")
    @Override
    Page<Comment> findAll (@NonNull Pageable pageable);

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.id = :id")
    @Override
    Optional<Comment> findById (@NonNull @Param("id") Long id);

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.user.id = :user_id")
    Page<Comment> findAllByUserId (@NonNull @Param("user_id") Long userId,Pageable pageable);

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.user.email = :user_email")
    Page<Comment> findAllByUserEmail (@NonNull @Param("user_email") String userEmail,Pageable pageable);

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.post.id = :post_id")
    Page<Comment> findAllByPostId (@NonNull @Param("post_id") Long postId,Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM comment c " +
            "WHERE c.user.email = :user_email")
    void deleteByUserEmail(@NonNull @Param("user_email") String userEmail);

    @Modifying
    @Transactional
    @Query("DELETE FROM comment c " +
            "WHERE c.post.id = :post_id")
    void deleteByPostId(@NonNull @Param("post_id") Long postId);
}
