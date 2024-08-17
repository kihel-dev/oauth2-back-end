package com.oauth2;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * The entry point for the Spring Boot application.
 * This class initializes the application and starts the Spring context.
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting the Spring Boot application...");
        SpringApplication.run(Application.class, args);
        logger.info("Application started successfully.");
    }
}
