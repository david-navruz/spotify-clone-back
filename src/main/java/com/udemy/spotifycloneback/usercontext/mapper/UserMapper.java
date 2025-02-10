package com.udemy.spotifycloneback.usercontext.mapper;

import com.udemy.spotifycloneback.usercontext.domain.User;
import com.udemy.spotifycloneback.usercontext.dto.ReadUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO userToReadUserDTO(User user);
    ReadUserDTO readUserDTOToUser(User entity);
}
