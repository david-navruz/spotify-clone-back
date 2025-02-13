package com.udemy.spotifycloneback.catalogcontext.mapper;

import com.udemy.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SongContentDTO;
import com.udemy.spotifycloneback.catalogcontext.domain.Song;
import com.udemy.spotifycloneback.catalogcontext.domain.SongContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongContentMapper {

    @Mapping(source = "song.publicId", target = "publicId")
    SongContentDTO songContentToSongContentDTO(Song song);

    SongContent saveSongDTOToSong(SaveSongDTO songDTO);

}
