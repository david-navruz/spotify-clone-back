package com.udemy.spotifycloneback.usercontext.repository;

import com.udemy.spotifycloneback.usercontext.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
