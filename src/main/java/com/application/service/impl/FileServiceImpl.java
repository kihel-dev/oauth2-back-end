package com.application.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.application.model.File; 
import com.application.model.User;
import com.application.repository.FileRepository;
import com.application.repository.UserRepository;
import com.application.service.FileService;
import com.application.dto.FileDTO;
import com.application.mapper.FileMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the {@link FileService} interface.
 * 
 * This service handles operations related to file management including 
 * storing, retrieving files, and obtaining files by user.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileMapper fileDBMapper;

    /**
     * Stores an uploaded file associated with the user's email.
     *
     * @param file the file to be uploaded
     * @param email the email of the user uploading the file
     * @return a {@link FileDTO} containing information about the stored file
     * @throws IOException if an error occurs during file storage
     */
    @Override
    public FileDTO store(MultipartFile file, String email) throws IOException {
        if (file == null || email == null) {
            logger.error("File or email cannot be null.");
            throw new IllegalArgumentException("File and email must not be null.");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String contentType = file.getContentType();
        
        logger.info("Uploading file: {} with content type: {}", fileName, contentType); 

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new RuntimeException("User not found with email: " + email);
        }

        File fileDB = new File(fileName, contentType, file.getBytes(), user);
        File savedFile = fileRepository.save(fileDB);

        logger.info("File uploaded successfully: {}", savedFile);
        return fileDBMapper.toDTO(savedFile); 
    }

    /**
     * Retrieves a file by its unique ID.
     *
     * @param id the unique identifier of the file
     * @return the {@link File} object corresponding to the provided ID
     * @throws RuntimeException if the file is not found
     */
    @Override
    public File getFileById(String id) {
        if (id == null) {
            logger.error("File ID cannot be null");
            throw new IllegalArgumentException("File ID cannot be null");
        }

        logger.info("Retrieving file with ID: {}", id); 
        return fileRepository.findById(id).orElseThrow(() -> {
            logger.error("File not found: {}", id);
            return new RuntimeException("File not found: " + id);
        });
    }

    /**
     * Retrieves a list of files associated with a specific user.
     *
     * @param email the email of the user whose files are to be retrieved
     * @return a list of {@link FileDTO} objects representing the user's files
     * @throws RuntimeException if the user is not found
     */
    @Override
    public List<FileDTO> getFilesByUser(String email) {
        if (email == null) {
            logger.error("Email cannot be null");
            throw new IllegalArgumentException("Email cannot be null");
        }

        logger.info("Retrieving files for user with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new RuntimeException("User not found with email: " + email);
        }

        List<FileDTO> files = fileRepository.findByUser(user).stream()
                .map(fileDBMapper::toDTO)
                .collect(Collectors.toList());
        
        logger.info("Retrieved {} files for user: {}", files.size(), email);
        return files; 
    }
}
