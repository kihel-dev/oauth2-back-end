package com.oauth2.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Custom user details implementation that extends UserDetails and OAuth2User.
 * This class holds user information from OAuth2 authentication and implements
 * necessary methods required by the Spring Security framework.
 */
public class CustomUserDetails implements UserDetails, OAuth2User {
    private String email;
    private String displayName; 
    private String profilePicture; 
    private Map<String, Object> attributes; 

    /**
     * Constructor to initialize CustomUserDetails.
     *
     * @param email          the user's email
     * @param displayName    the user's display name
     * @param profilePicture  the URL of the user's profile picture
     * @param attributes     a map of OAuth2 user attributes
     */
    public CustomUserDetails(String email, String displayName, String profilePicture, Map<String, Object> attributes) {
        this.email = email != null ? email : "default@example.com"; 
        this.displayName = displayName != null ? displayName : "Default Display Name"; 
        this.profilePicture = profilePicture != null ? profilePicture : "https://default-image-url.com"; 
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Return roles if applicable, currently returning null
    }

    @Override
    public String getPassword() {
        return null; // No password used in JWT authentication
    }

    @Override
    public String getUsername() {
        return email; // Email is treated as the unique identifier
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is considered non-expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is considered non-locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are considered non-expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is enabled
    }

    /**
     * Returns the attributes of the authenticated user.
     *
     * @return a map of OAuth2 user attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // Return the stored OAuth2 attributes
    }

    /**
     * Returns the display name of the user.
     *
     * @return the display name
     */
    @Override
    public String getName() {
        return displayName; // Return the user's display name
    }

    /**
     * Gets the display name of the user.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName; // Return the display name
    }

    /**
     * Gets the URL of the user's profile picture.
     * 
     * @return the profile picture URL
     */
    public String getProfilePicture() {
        return profilePicture; // Return the profile picture URL
    }
}
