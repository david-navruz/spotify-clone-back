package com.udemy.spotifycloneback.catalogcontext.application;

import com.udemy.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SongContentDTO;
import com.udemy.spotifycloneback.catalogcontext.domain.Song;
import com.udemy.spotifycloneback.catalogcontext.domain.SongContent;
import com.udemy.spotifycloneback.catalogcontext.mapper.SongContentMapper;
import com.udemy.spotifycloneback.catalogcontext.mapper.SongMapper;
import com.udemy.spotifycloneback.catalogcontext.repository.FavoriteRepository;
import com.udemy.spotifycloneback.catalogcontext.repository.SongContentRepository;
import com.udemy.spotifycloneback.catalogcontext.repository.SongRepository;
import com.udemy.spotifycloneback.usercontext.application.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class SongService {

    private final SongRepository songRepository;

    private final SongMapper songMapper;

    private final SongContentRepository songContentRepository;

    private final SongContentMapper songContentMapper;

    private final UserService userService;

    private final FavoriteRepository favoriteRepository;


    public SongService(SongRepository songRepository, SongMapper songMapper, SongContentRepository songContentRepository,
                       SongContentMapper songContentMapper, UserService userService,
                       FavoriteRepository favoriteRepository) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
        this.songContentRepository = songContentRepository;
        this.songContentMapper = songContentMapper;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
    }


    public ReadSongInfoDTO create(SaveSongDTO saveSongDTO) {
        Song song = songMapper.saveSongDTOToSong(saveSongDTO);
        Song songSaved = null;
        if (song != null) {
            songSaved = songRepository.save(song);
        }

        SongContent songContent = songContentMapper.saveSongDTOToSong(saveSongDTO);
        if (songContent != null) {
            songContent.setSong(songSaved);
            songContentRepository.save(songContent);
        }
        return songMapper.songToReadSongInfoDTO(songSaved);
    }

    @Transactional(readOnly = true)
    public List<ReadSongInfoDTO> getAll() {
        List<ReadSongInfoDTO> allSongs = songRepository.findAll()
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();

   /*     if (userService.isAuthenticated()) {
            fetch

        }*/

        return allSongs;
    }

    public Optional<SongContentDTO> getOneByPublicId(UUID publicId) {
        Optional<SongContent> songContent = songContentRepository.findOneBySongPublicId(publicId);
            if (songContent.isPresent()) {
                return songContent.map(songContentMapper::songContentToSongContentDTO);
            }
        return Optional.empty();
    }






}
