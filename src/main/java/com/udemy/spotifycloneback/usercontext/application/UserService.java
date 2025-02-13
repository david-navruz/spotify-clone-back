package com.udemy.spotifycloneback.usercontext.application;

import com.udemy.spotifycloneback.usercontext.domain.User;
import com.udemy.spotifycloneback.usercontext.application.dto.ReadUserDTO;
import com.udemy.spotifycloneback.usercontext.mapper.UserMapper;
import com.udemy.spotifycloneback.usercontext.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public void syncWithIdp(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User user = mapOauth2AttributesToUser(attributes);
        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            if (attributes.get("updated_at") != null) {
                Instant dbLastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
                Instant idpModifiedDate;
                if(attributes.get("updated_at") instanceof Instant) {
                    idpModifiedDate = (Instant) attributes.get("updated_at");
                } else {
                    idpModifiedDate = Instant.ofEpochSecond((Integer) attributes.get("updated_at"));
                }
                if(idpModifiedDate.isAfter(dbLastModifiedDate)) {
                    updateUser(user);
                }
            }
        } else {
            userRepository.saveAndFlush(user);
        }
    }


    private void updateUser(User user) {
        Optional<User> userToUpdate = userRepository.findOneByEmail(user.getEmail());
            if (userToUpdate.isPresent()) {
                User updatedUser = userToUpdate.get();
                updatedUser.setEmail(user.getEmail());
                updatedUser.setFirstName(user.getFirstName());
                updatedUser.setLastName(user.getLastName());
                updatedUser.setImageUrl(user.getImageUrl());
                userRepository.saveAndFlush(updatedUser);
            }
    }


    public ReadUserDTO getAuthenticatedUserFromSecurityContext() {
        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = mapOauth2AttributesToUser(principal.getAttributes());
        return userMapper.userToReadUserDTO(user);
    }


    private User mapOauth2AttributesToUser(Map<String, Object> attributes){
        User user = new User();
        String sub = String.valueOf(attributes.get("sub"));

        String username = null;

        if (Objects.nonNull(attributes.get("preferred_username"))) {
            username = attributes.get("preferred_username").toString();
        }

        if (Objects.nonNull(attributes.get("given_name"))) {
            user.setFirstName(attributes.get("given_name").toString());
        } else if (Objects.nonNull(attributes.get("name"))) {
            user.setFirstName(attributes.get("name").toString());
        }

        if (Objects.nonNull(attributes.get("family_name"))) {
            user.setLastName(attributes.get("family_name").toString());
        }

        if (Objects.nonNull(attributes.get("email"))) {
            user.setEmail(attributes.get("email").toString());
        } else if ( (sub.contains("|")) && (username != null && username.contains("@")) ) {
            user.setEmail(username);
        }

        if (Objects.nonNull(attributes.get("picture"))) {
            user.setImageUrl((String) attributes.get("picture").toString());
        }

        return user;
    }

    public Optional<ReadUserDTO> getByEmail(String email) {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::userToReadUserDTO);
    }

    public boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }


}
