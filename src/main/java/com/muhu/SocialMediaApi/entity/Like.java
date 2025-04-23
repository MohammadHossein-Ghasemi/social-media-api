package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "like")
@Table(name = "user_like")
public class Like {
    @Id
    @SequenceGenerator(sequenceName = "like_seq",name = "l_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "l_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    @NotNull
    private Post post;
}
