package com.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.application.dto.UserDTO;
import com.application.mapper.impl.UserMapperImpl;
import com.application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void testToDTO_NullInput() {
        UserDTO result = userMapper.toDTO(null);
        assertNull(result, "Expected null when input is null");
    }

    @Test
    public void testToDTO_ValidInput() {
        User user = new User("1", "test@example.com", "Test User", "http://example.com/pic.jpg");
        UserDTO result = userMapper.toDTO(user);

        assertEquals(user.getDisplayName(), result.displayName());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getProfilePicture(), result.profilePicture());
    }

    @Test
    public void testToModel_NullInput() {
        User result = userMapper.toModel(null);
        assertNull(result, "Expected null when input is null");
    }

    @Test
    public void testToModel_ValidInput() {
        UserDTO userDTO = new UserDTO("Test User", "test@example.com", "http://example.com/pic.jpg");
        User result = userMapper.toModel(userDTO);

        assertEquals(userDTO.displayName(), result.getDisplayName());
        assertEquals(userDTO.email(), result.getEmail());
        assertEquals(userDTO.profilePicture(), result.getProfilePicture());
    }
}
