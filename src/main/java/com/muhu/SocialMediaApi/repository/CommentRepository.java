package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment , Long> {

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post ")
    @Override
    List<Comment> findAll ();

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
    List<Comment> findByUserId (@NonNull @Param("user_id") Long userId);

    @Query("SELECT c FROM comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.post.id = :post_id")
    List<Comment> findByPostId (@NonNull @Param("post_id") Long postId);
}
