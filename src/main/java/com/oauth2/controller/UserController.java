package com.oauth2.controller;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth2.dto.UserDTO;
import com.oauth2.security.CustomUserDetails;

/**
 * Controller responsible for user-related actions.
 * This class handles the retrieval of user information.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Retrieves the information of the authenticated user.
     *
     * @return ResponseEntity containing UserDTO with user details if the user is authenticated
     * @throws RuntimeException if the user is not authenticated
     */
    @GetMapping
    public ResponseEntity<UserDTO> getUserInfo() {
        logger.info("Fetching user information.");

        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            logger.error("User not authenticated");
            throw new RuntimeException("User not authenticated"); // Exception if user is not authenticated.
        }

        // Get the user details from the authenticated principal
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDTO user = new UserDTO(userDetails.getDisplayName(), userDetails.getUsername(), userDetails.getProfilePicture());

        logger.info("User Info fetched: {}", user); // Log the fetched user info
        return ResponseEntity.ok(user); // Wrap UserDTO in ResponseEntity
    }
}
