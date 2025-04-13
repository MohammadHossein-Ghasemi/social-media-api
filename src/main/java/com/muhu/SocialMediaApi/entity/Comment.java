package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comment")
@Table(name = "comment")
public class Comment {
    @Id
    @SequenceGenerator(sequenceName = "comment_seq",name = "c_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "c_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @NotBlank(message = "Please enter content.")
    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
