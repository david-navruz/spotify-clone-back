package com.udemy.spotifycloneback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.udemy.spotifycloneback")
public class SpotifyCloneBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyCloneBackApplication.class, args);
    }

}
