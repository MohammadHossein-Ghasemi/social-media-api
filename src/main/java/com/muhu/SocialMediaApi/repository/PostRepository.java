package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post , Long> {

    @Query("SELECT p FROM post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.likes ")
    @Override
    Page<Post> findAll (@NonNull Pageable pageable);

    @Query("SELECT p FROM post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.likes " +
            "WHERE p.id = :id")
    @Override
    Optional<Post> findById (@NonNull @Param("id") Long id);

    @Query("SELECT p FROM post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.likes " +
            "WHERE p.user.id = :user_id")
    Page<Post> findByUserId (@NonNull @Param("user_id") Long userId,Pageable pageable);

    @Query("SELECT p FROM post p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.comments " +
            "LEFT JOIN FETCH p.likes " +
            "WHERE p.user.email = :user_email")
    Page<Post> findByUserEmail (@NonNull @Param("user_email") String userEmail,Pageable pageable);
}
