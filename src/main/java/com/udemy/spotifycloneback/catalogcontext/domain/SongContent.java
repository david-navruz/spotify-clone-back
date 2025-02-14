package com.udemy.spotifycloneback.catalogcontext.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "song_content")
public class SongContent implements Serializable {

    /**
     * song_id column in SongContent is both a primary key and a foreign key.
     */
    @Id
    @Column(name = "song_id")
    private Long songId;


    /**
     * "song_id" the foreign key column in the current table
     * referencedColumnName = "id", the id column of the Song entity.
     */
    @MapsId
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    @OneToOne
    private Song song;


    /**
     * @Lob Marks the file field as a large object to store binary data
     */
    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;


    @Column(name = "file_content_type")
    private String fileContentType;
    
}
