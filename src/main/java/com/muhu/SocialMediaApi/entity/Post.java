package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
@Table(name = "post")
public class Post {
    @Id
    @SequenceGenerator(sequenceName = "post_seq",name = "p_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "p_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @NotBlank(message = "Please enter content.")
    @Column(nullable = false)
    private String content;

    private String imageUrl;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<Like> likes;
}
