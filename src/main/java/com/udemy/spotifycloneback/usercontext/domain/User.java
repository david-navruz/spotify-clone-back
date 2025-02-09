package com.udemy.spotifycloneback.usercontext.domain;

import com.udemy.spotifycloneback.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "spotify_user")
public class User extends AbstractAuditingEntity<Long> {


        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
        @SequenceGenerator(name = "userSequenceGenerator", sequenceName = "user_generator", allocationSize = 1)
        private Long id;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "email")
        private String email;

        @Column(name = "image_url")
        private String imageUrl;

        private Subscription subscription = Subscription.FREE;



}
