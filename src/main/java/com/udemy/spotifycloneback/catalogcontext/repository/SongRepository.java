package com.udemy.spotifycloneback.catalogcontext.repository;

import com.udemy.spotifycloneback.catalogcontext.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {



}
