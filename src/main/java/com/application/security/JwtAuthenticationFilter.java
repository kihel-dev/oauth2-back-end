package com.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.application.dto.UserDTO;
import com.application.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * JWT Authentication Filter that processes incoming HTTP requests to authenticate users based on the JWT token.
 * Extracts the token from cookies, validates it, and sets the authentication in the security context.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final TokenProvider tokenProvider;
    private final UserService userService;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token != null && tokenProvider.validateToken(token)) {
            String email = tokenProvider.getUsername(token); // Get email as the unique identifier

            // Fetch user by email
            UserDTO userDTO = userService.getUserByEmail(email);
            if (userDTO != null) {
                // Create CustomUserDetails object with userDTO attributes
                Map<String, Object> attributes = Map.of(
                        "email", userDTO.email(),
                        "name", userDTO.displayName(),
                        "picture", userDTO.profilePicture()
                );

                CustomUserDetails userDetails = new CustomUserDetails(userDTO.email(), userDTO.displayName(), userDTO.profilePicture(), attributes);
                
                // Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User {} has been authenticated successfully.", email);
            } else {
                logger.warn("Authenticated user not found in the database for email: {}", email);
            }
        } else {
            logger.warn("No valid JWT token found in request.");
        }

        filterChain.doFilter(request, response); // Continue with the next filter in the chain
    }

    /**
     * Extracts the JWT token from the HTTP request cookies.
     *
     * @param request the HTTP request
     * @return the JWT token if found, null otherwise
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            logger.debug("Number of cookies found: {}", cookies.length);
            for (Cookie cookie : cookies) {
                logger.debug("Checking cookie with name: {}", cookie.getName());
                if ("JWT".equals(cookie.getName())) {
                    logger.debug("JWT cookie found: {}", cookie.getValue());
                    return cookie.getValue(); // Return the JWT when found
                }
            }
        } else {
            logger.warn("No cookies found in the request.");
        }
        return null; // No token found
    }
}
