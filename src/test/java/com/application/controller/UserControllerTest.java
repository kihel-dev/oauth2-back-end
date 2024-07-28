package com.application.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.application.dto.UserDTO;
import com.application.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController();
        
        // Set up a mock authentication
        CustomUserDetails userDetails = new CustomUserDetails("user@example.com", "User Name", "profilePicUrl", null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetUserInfo_Success() {
        UserDTO expectedUser = new UserDTO("User Name", "user@example.com", "profilePicUrl");

        ResponseEntity<UserDTO> response = userController.getUserInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    public void testGetUserInfo_NotAuthenticated() {
        // Clear the SecurityContext to simulate no authentication
        SecurityContextHolder.clearContext();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getUserInfo();
        });

        assertEquals("User not authenticated", exception.getMessage());
    }
}
