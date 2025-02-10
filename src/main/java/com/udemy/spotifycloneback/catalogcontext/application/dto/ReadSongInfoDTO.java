package com.udemy.spotifycloneback.catalogcontext.application.dto;

import com.udemy.spotifycloneback.catalogcontext.application.vo.SongAuthorVO;
import com.udemy.spotifycloneback.catalogcontext.application.vo.SongTitleVO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class ReadSongInfoDTO {

    private SongTitleVO title;

    private SongAuthorVO author;

    @NotNull
    private byte[] cover;

    @NotNull
    private String coverContentType;

    @NotNull
    private boolean isFavorite;

    @NotNull
    private UUID publicId;

}
