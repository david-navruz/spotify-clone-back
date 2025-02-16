package com.udemy.spotifycloneback.catalogcontext.application;

import com.udemy.spotifycloneback.catalogcontext.application.dto.FavoriteSongDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SongContentDTO;
import com.udemy.spotifycloneback.catalogcontext.domain.Favorite;
import com.udemy.spotifycloneback.catalogcontext.domain.FavoriteId;
import com.udemy.spotifycloneback.catalogcontext.domain.Song;
import com.udemy.spotifycloneback.catalogcontext.domain.SongContent;
import com.udemy.spotifycloneback.catalogcontext.mapper.SongContentMapper;
import com.udemy.spotifycloneback.catalogcontext.mapper.SongMapper;
import com.udemy.spotifycloneback.catalogcontext.repository.FavoriteRepository;
import com.udemy.spotifycloneback.catalogcontext.repository.SongContentRepository;
import com.udemy.spotifycloneback.catalogcontext.repository.SongRepository;
import com.udemy.spotifycloneback.infrastructure.service.dto.State;
import com.udemy.spotifycloneback.infrastructure.service.dto.StateBuilder;
import com.udemy.spotifycloneback.usercontext.application.UserService;

import com.udemy.spotifycloneback.usercontext.application.dto.ReadUserDTO;
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

        if (userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(allSongs);
        }
        return allSongs;
    }

    public Optional<SongContentDTO> getOneByPublicId(UUID publicId) {
        Optional<SongContent> songContent = songContentRepository.findOneBySongPublicId(publicId);
        if (songContent.isPresent()) {
            return songContent.map(songContentMapper::songContentToSongContentDTO);
        }
        return Optional.empty();
    }


    public List<ReadSongInfoDTO> search(String searchTerm) {
        List<ReadSongInfoDTO> searchedSongs = songRepository.findByTitleOrAuthorContaining(searchTerm)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();

        if (userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(searchedSongs);
        } else {
            return searchedSongs;
        }
    }


    public State<FavoriteSongDTO, String> addOrRemoveFromFavorite(FavoriteSongDTO favoriteSongDTO, String email) {
        StateBuilder<FavoriteSongDTO, String> builder = State.builder();
        Optional<Song> songToLikeOpt = songRepository.findOneByPublicId(favoriteSongDTO.publicId());
        if (songToLikeOpt.isEmpty()) {
            return builder.forError("Song public id doesn't exist").build();
        }

        Song songToLike = songToLikeOpt.get();

        ReadUserDTO userWhoLikedSong = userService.getByEmail(email).orElseThrow();

        if (favoriteSongDTO.favorite()) {
            Favorite favorite = new Favorite();
            favorite.setSongPublicId(songToLike.getPublicId());
            favorite.setUserEmail(userWhoLikedSong.email());
            favoriteRepository.save(favorite);
        } else {
            FavoriteId favoriteId = new FavoriteId(songToLike.getPublicId(), userWhoLikedSong.email());
            favoriteRepository.deleteById(favoriteId);
            favoriteSongDTO = new FavoriteSongDTO(false, songToLike.getPublicId());
        }

        return builder.forSuccess(favoriteSongDTO).build();
    }


    public List<ReadSongInfoDTO> fetchFavoriteSongs(String email) {
        return songRepository.findAllFavoriteByUserEmail(email)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();
    }

    private List<ReadSongInfoDTO> fetchFavoritesStatusForSongs(List<ReadSongInfoDTO> songs) {
        ReadUserDTO authenticatedUser = userService.getAuthenticatedUserFromSecurityContext();

        List<UUID> songPublicIds = songs.stream().map(ReadSongInfoDTO::getPublicId).toList();

        List<UUID> userFavoriteSongs = favoriteRepository.findAllByUserEmailAndSongPublicIdIn(authenticatedUser.email(), songPublicIds)
                .stream().map(Favorite::getSongPublicId).toList();

        return songs.stream().peek(song -> {
            if (userFavoriteSongs.contains(song.getPublicId())) {
                song.setFavorite(true);
            }
        }).toList();
    }

}
