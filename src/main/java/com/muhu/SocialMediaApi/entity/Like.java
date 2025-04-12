package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "like")
@Table(name = "like")
public class Like {
    @Id
    @SequenceGenerator(sequenceName = "like_seq",name = "l_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "l_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
}
