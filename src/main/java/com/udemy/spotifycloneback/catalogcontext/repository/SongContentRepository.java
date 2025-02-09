package com.udemy.spotifycloneback.catalogcontext.repository;

import com.udemy.spotifycloneback.catalogcontext.domain.SongContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongContentRepository extends JpaRepository<SongContent, Long> {

}
