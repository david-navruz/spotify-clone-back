package com.udemy.spotifycloneback.catalogcontext.repository;

import com.udemy.spotifycloneback.catalogcontext.domain.Favorite;
import com.udemy.spotifycloneback.catalogcontext.domain.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {


}
