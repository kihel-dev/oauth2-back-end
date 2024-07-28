package com.application.dto;

/**
 * Data Transfer Object (DTO) for File.
 *
 * This DTO encapsulates file information that can be transferred between different layers of the application.
 * It includes the unique identifier, name, and MIME type of the file.
 *
 * @param id the unique identifier of the file
 * @param name the name of the file
 * @param type the MIME type of the file (e.g., "image/png")
 */
public record FileDTO(String id, String name, String type) {}
