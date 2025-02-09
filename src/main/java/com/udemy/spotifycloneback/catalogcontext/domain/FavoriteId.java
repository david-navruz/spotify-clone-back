package com.udemy.spotifycloneback.catalogcontext.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteId implements Serializable {

    UUID songPublicId;

    String userEmail;

}
