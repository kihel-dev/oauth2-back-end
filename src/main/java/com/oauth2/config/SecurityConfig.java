package com.oauth2.config;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.oauth2.security.JwtAuthenticationFilter;
import com.oauth2.security.TokenProvider;
import com.oauth2.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain...");

        http
            .csrf().disable()
            .exceptionHandling(exceptionHandling -> {
                exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            })
            .sessionManagement(sessionManagement -> {
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(authorize -> {
                authorize
                    .requestMatchers("/", "/auth/**", "/public/**").permitAll()
                    .anyRequest().authenticated();
            })
            .oauth2Login(oauth2Login -> {
                oauth2Login
                    .successHandler((request, response, authentication) -> {
                        String token = tokenProvider.createToken(authentication);
                        Cookie cookie = new Cookie("JWT", token);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(false); // Set to true if using HTTPS
                        cookie.setPath("/");
                        cookie.setMaxAge(604800);
                        response.addCookie(cookie);
                        logger.info("JWT cookie added to response for user: {}", authentication.getName());
                        response.sendRedirect("http://localhost:4200/dashboard");
                    })
                    .failureUrl("/login?error=true");
            })
            .logout(logout -> logout
                    .logoutSuccessHandler((request, response, authentication) -> {
                        logger.info("User logged out successfully: {}", (authentication != null ? authentication.getName() : "Anonymous"));
                        Cookie cookie = new Cookie("JWT", null);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(false); 
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    })
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        logger.info("Security filter chain configured successfully.");
        return http.build();
    }
}
