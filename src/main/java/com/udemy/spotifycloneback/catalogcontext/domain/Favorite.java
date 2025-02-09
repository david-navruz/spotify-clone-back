package com.udemy.spotifycloneback.catalogcontext.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "favorite_song")
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    private UUID songPublicId;

    @Id
    @Column(name = "user_email")
    private String userEmail;

}
