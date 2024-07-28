package com.application.mapper;

import com.application.dto.UserDTO;
import com.application.model.User;

/**
 * Mapper interface for converting between {@link User} and {@link UserDTO}.
 *
 * This interface provides methods for mapping User entities to UserDTO objects 
 * and vice versa, facilitating data transfer between different layers of the application.
 */
public interface UserMapper {

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the {@link User} entity to convert
     * @return the corresponding {@link UserDTO} object, or {@code null} if the input is {@code null}
     */
    UserDTO toDTO(User user);

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDTO the {@link UserDTO} object to convert
     * @return the corresponding {@link User} entity, or {@code null} if the input is {@code null}
     */
    User toModel(UserDTO userDTO);
}
