package com.muhu.SocialMediaApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Entity(name = "user")
@Table(name = "user_")
public class User {
    @Id
    @SequenceGenerator(name = "u_seq",sequenceName = "user_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "u_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    @NotBlank(message = "Please enter a username.")
    @Column(unique = true,nullable = false)
    private String username;

    @Email(message = "Please enter valid email.")
    @NotBlank(message = "Please enter an email.")
    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;

    private String profilePictureUrl;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers;

    @ManyToMany
    @JoinTable(
            name = "following",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Like> likes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Authority> authorities;

}
