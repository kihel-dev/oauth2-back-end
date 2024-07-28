package com.application.service;

import java.io.IOException;
import java.util.List;

import com.application.dto.FileDTO;
import com.application.model.File;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing file operations.
 * 
 * This interface defines methods for storing files, retrieving files by ID, 
 * and getting a list of files associated with a specific user.
 */
public interface FileService {

    /**
     * Stores an uploaded file associated with the given user's email.
     *
     * @param file the file to be uploaded
     * @param email the email of the user uploading the file
     * @return a {@link FileDTO} containing information about the stored file
     * @throws IOException if an error occurs during file storage
     */
    FileDTO store(MultipartFile file, String email) throws IOException;

    /**
     * Retrieves a file by its unique ID.
     *
     * @param id the unique identifier of the file
     * @return the {@link File} object corresponding to the provided ID
     *         or null if the file is not found
     */
    File getFileById(String id);

    /**
     * Retrieves a list of files associated with a specific user.
     *
     * @param email the email of the user whose files are to be retrieved
     * @return a list of {@link FileDTO} objects representing the user's files
     */
    List<FileDTO> getFilesByUser(String email);
}
