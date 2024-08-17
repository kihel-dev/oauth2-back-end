package com.oauth2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

/**
 * Utility class for managing JWT tokens.
 * Provides methods for creating, validating, and extracting information from JWTs.
 */
@Component
public class TokenProvider {
    private final String JWT_SECRET;
    private final long JWT_EXPIRATION = 604800000L; // 7 days in milliseconds

    /**
     * Constructor that generates a secure random key for signing the JWTs.
     */
    public TokenProvider() {
        byte[] randomKey = new byte[64];
        new SecureRandom().nextBytes(randomKey);
        this.JWT_SECRET = Base64.getEncoder().encodeToString(randomKey);
    }

    /**
     * Creates a JWT token given an Authentication object.
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT token as a string
     */
    public String createToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
        }
        return false; // Token is invalid
    }

    /**
     * Extracts claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) contained in the token
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
