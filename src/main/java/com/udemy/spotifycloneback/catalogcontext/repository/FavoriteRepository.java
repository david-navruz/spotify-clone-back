package com.udemy.spotifycloneback.catalogcontext.repository;

import com.udemy.spotifycloneback.catalogcontext.domain.Favorite;
import com.udemy.spotifycloneback.catalogcontext.domain.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findAllByUserEmailAndSongPublicIdIn(String email, List<UUID> songPublicIds);
}
