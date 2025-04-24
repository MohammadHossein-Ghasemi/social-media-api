package com.muhu.SocialMediaApi.repository;

import com.muhu.SocialMediaApi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.posts " +
            "LEFT JOIN FETCH u.followers " +
            "LEFT JOIN FETCH u.following " +
            "LEFT JOIN FETCH u.likes " +
            "LEFT JOIN FETCH u.notifications " +
            "LEFT JOIN FETCH u.comments ")
    @Override
    List<User> findAll() ;

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.posts " +
            "LEFT JOIN FETCH u.followers " +
            "LEFT JOIN FETCH u.following " +
            "LEFT JOIN FETCH u.likes " +
            "LEFT JOIN FETCH u.notifications " +
            "LEFT JOIN FETCH u.comments " +
            "WHERE u.id = :userId")
    @Override
    Optional<User> findById (@NonNull @Param("userId") Long userId);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.posts " +
            "LEFT JOIN FETCH u.followers " +
            "LEFT JOIN FETCH u.following " +
            "LEFT JOIN FETCH u.likes " +
            "LEFT JOIN FETCH u.notifications " +
            "LEFT JOIN FETCH u.comments " +
            "WHERE u.username = :username")
    Optional<User> findByUsername(@NonNull @Param("username") String username);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.posts " +
            "LEFT JOIN FETCH u.followers " +
            "LEFT JOIN FETCH u.following " +
            "LEFT JOIN FETCH u.likes " +
            "LEFT JOIN FETCH u.notifications " +
            "LEFT JOIN FETCH u.comments " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(@NonNull @Param("email") String email);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.followers " +
            "WHERE u.id = :user_id" )
    List<User> findAllFollowersById(@NonNull @Param("user_id") Long userId);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.following "+
            "WHERE u.id = :user_id")
    List<User> findAllFollowingById(@NonNull @Param("user_id") Long userId);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.followers " +
            "WHERE u.email = :email" )
    List<User> findAllFollowersByEmail(@NonNull @Param("email") String email);

    @Query("SELECT u FROM user u " +
            "LEFT JOIN FETCH u.following "+
            "WHERE u.email = :email")
    List<User> findAllFollowingByEmail(@NonNull @Param("email") String email);

    boolean existsByEmail(@NonNull String email);

    void deleteByEmail(String email);
}
