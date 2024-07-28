package com.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.application.dto.UserDTO;
import com.application.mapper.UserMapper;
import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.service.UserService;
import com.application.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserByEmail_UserFound() {
        User user = new User("1", "test@example.com", "Test User", "http://example.com/pic.jpg");
        UserDTO userDTO = new UserDTO("Test User", "test@example.com", "http://example.com/pic.jpg");

        doReturn(user).when(userRepository).findByEmail("test@example.com");
        doReturn(userDTO).when(userMapper).toDTO(user);

        UserDTO result = userService.getUserByEmail("test@example.com");

        assertEquals(userDTO, result, "The UserDTO should match the expected value");
    }

    @Test
    public void testGetUserByEmail_UserNotFound() {
        doReturn(null).when(userRepository).findByEmail("nonexistent@example.com");

        UserDTO result = userService.getUserByEmail("nonexistent@example.com");

        assertNull(result, "The result should be null if the user is not found");
    }

    @Test
    public void testSaveUser() {
        UserDTO userDTO = new UserDTO("Test User", "test@example.com", "http://example.com/pic.jpg");
        User user = new User("1", "test@example.com", "Test User", "http://example.com/pic.jpg");

        doReturn(user).when(userMapper).toModel(userDTO);

        userService.saveUser(userDTO);

        verify(userMapper).toModel(userDTO);
        verify(userRepository).save(user);
    }
}
