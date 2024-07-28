package com.application.dto;

/**
 * Data Transfer Object (DTO) for User.
 * 
 * This DTO contains user information that can be transferred between different layers of the application.
 * It encapsulates the user's display name, email address, and profile picture URL.
 *
 * Use this DTO to pass user data within services, controllers, and other components.
 *
 * @param displayName the display name of the user
 * @param email the email address of the user
 * @param profilePicture the URL to the user's profile picture
 */
public record UserDTO(
    String displayName,
    String email,
    String profilePicture
) {
}
