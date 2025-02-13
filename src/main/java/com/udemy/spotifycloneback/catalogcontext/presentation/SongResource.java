package com.udemy.spotifycloneback.catalogcontext.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.spotifycloneback.catalogcontext.application.SongService;
import com.udemy.spotifycloneback.catalogcontext.application.dto.ReadSongInfoDTO;
import com.udemy.spotifycloneback.catalogcontext.application.dto.SaveSongDTO;
import com.udemy.spotifycloneback.usercontext.application.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class SongResource {

    private final SongService songService;

    private final UserService userService;

    private final Validator validator;
    private final ObjectMapper objectMapper;


    public SongResource(SongService songService, UserService userService, Validator validator, ObjectMapper objectMapper) {
        this.songService = songService;
        this.userService = userService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }


    @PostMapping(value = "/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReadSongInfoDTO> add(@RequestPart(name = "cover") MultipartFile cover,
                                               @RequestPart(name = "file") MultipartFile file,
                                               @RequestPart(name = "dto") String saveSongDTOString) throws IOException {

        SaveSongDTO saveSongDTO = objectMapper.readValue(saveSongDTOString, SaveSongDTO.class);
        saveSongDTO = new SaveSongDTO(saveSongDTO.title(), saveSongDTO.author(),
                cover.getBytes(), cover.getContentType(), file.getBytes(), file.getContentType()
        );

        /** javax.validation.Validator checks the SaveSongDTO fields against constraints (e.g., @NotNull, @Size).
         *  If there are validation errors, a 400 Bad Request response is returned with the details.
         */
        Set<ConstraintViolation<SaveSongDTO>> violations = validator.validate(saveSongDTO);
        if (!violations.isEmpty()) {
            String violationsJoined = violations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "Validation errors for the fields : " + violationsJoined);

            return ResponseEntity.of(validationIssue).build();

        } else {
            /** If validation passes, the song is saved
             *  Returns 200 OK with the created song details.
             */
            return ResponseEntity.ok(songService.create(saveSongDTO));
        }
    }






}
