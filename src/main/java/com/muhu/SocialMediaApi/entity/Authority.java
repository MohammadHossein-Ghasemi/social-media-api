package com.muhu.SocialMediaApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "authority")
@Table(name = "authority")
public class Authority {
    @Id
    @SequenceGenerator(name = "a_seq",sequenceName = "authority_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "a_seq")
    @Column(unique = true,updatable = false,nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
