package com.udemy.spotifycloneback.catalogcontext.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "song")
public class Song implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "songSequenceGenerator")
    @SequenceGenerator(name = "songSequenceGenerator", sequenceName = "song_generator", allocationSize = 1)
    private Long id;

    @UuidGenerator
    @Column(name = "public_id")
    private UUID publicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Lob // large object
    @Column(name = "cover", nullable = false)
    private byte[] cover;

    @Column(name = "cover_content_type", nullable = false)
    private String coverContentType;

}
