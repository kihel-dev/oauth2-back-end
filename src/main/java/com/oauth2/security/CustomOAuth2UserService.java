package com.oauth2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.oauth2.dto.UserDTO;
import com.oauth2.service.UserService;

import java.util.Map;

/**
 * Custom service for loading user details from OAuth2 providers.
 * This service extends the DefaultOAuth2UserService to add custom processing for user details.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserService userService;

    /**
     * Loads the user details from the OAuth2 user request.
     *
     * @param userRequest the OAuth2 user request containing the user's details
     * @return the loaded OAuth2User with user information
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        logger.info("Loading user from OAuth2 user request...");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // Google, GitHub, Facebook, etc.

        String email = null;
        String displayName = null;
        String profilePicture = null;

        // Extracting user attributes based on the provider
        switch (provider) {
            case "google":
                email = (String) attributes.get("email");
                displayName = (String) attributes.get("name");
                profilePicture = (String) attributes.get("picture");
                logger.debug("Google user attributed fetched: email={}, displayName={}, profilePicture={}", email, displayName, profilePicture);
                break;
            case "github":
                email = (String) attributes.get("login");
                displayName = (String) attributes.get("login");
                profilePicture = (String) attributes.get("avatar_url");
                logger.debug("GitHub user attributed fetched: email={}, displayName={}, profilePicture={}", email, displayName, profilePicture);
                break;
            default:
                logger.warn("Unsupported provider: {}", provider);
                break;
        }

        logger.debug("Provider: {}, Extracted user attributes: email={}, displayName={}, profilePicture={}",
                     provider, email, displayName, profilePicture);

        // Create or update the user in the database
        UserDTO userDTO = userService.getUserByEmail(email);
        if (userDTO == null) {
            userDTO = new UserDTO(displayName, email, profilePicture);
            userService.saveUser(userDTO);
            logger.info("Registered new user: {}", userDTO);
        } else {
            logger.info("User already exists: {}", userDTO);
        }

        return new CustomUserDetails(email, displayName, profilePicture, attributes);
    }
}
