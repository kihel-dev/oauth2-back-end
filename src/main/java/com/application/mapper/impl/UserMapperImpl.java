package com.application.mapper.impl;

import com.application.dto.UserDTO;
import com.application.mapper.UserMapper;
import com.application.model.User;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link UserMapper} interface for converting between {@link User} entities and {@link UserDTO} objects.
 * 
 * This class provides manual mapping logic to transform User entities to UserDTOs and vice versa.
 */
@Component
public class UserMapperImpl implements UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDTO}.
     *
     * @param user the {@link User} entity to convert
     * @return the corresponding {@link UserDTO} object, or null if the input user is null
     */
    @Override
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null; // Return null if the input user entity is null
        }
        UserDTO userDTO = new UserDTO(user.getDisplayName(), user.getEmail(), user.getProfilePicture());

        // Optional: Log the conversion
        return userDTO;
    }

    /**
     * Converts a {@link UserDTO} to a {@link User} entity.
     *
     * @param userDTO the {@link UserDTO} object to convert
     * @return the corresponding {@link User} entity, or null if the input userDTO is null
     */
    @Override
    public User toModel(UserDTO userDTO) {
        if (userDTO == null) {
            return null; // Return null if the input userDTO is null
        }
        User user = new User();
        user.setDisplayName(userDTO.displayName());
        user.setEmail(userDTO.email());
        user.setProfilePicture(userDTO.profilePicture());

        return user;
    }
}
