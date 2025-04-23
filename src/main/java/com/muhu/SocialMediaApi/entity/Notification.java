package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notification")
@Table(name = "notification")
public class Notification {
    @Id
    @SequenceGenerator(sequenceName = "notification_seq",name = "n_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "n_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @NotBlank(message = "Please enter message.")
    @Column(nullable = false)
    private String message;

    @Column(columnDefinition = "false",nullable = false)
    private Boolean isRead;

    @CreationTimestamp
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @NotNull
    private User user;
}
