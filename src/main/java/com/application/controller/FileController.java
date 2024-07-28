package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.dto.FileDTO;
import com.application.model.File;
import com.application.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing files.
 * 
 * This controller provides endpoints for uploading files, retrieving a list of files
 * for the authenticated user, and downloading files by ID.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * Endpoint for uploading a file.
     *
     * @param file the file to upload
     * @param authentication the authentication object containing user details
     * @return ResponseEntity containing the uploaded FileDTO or error response
     */
    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        logger.info("Attempting to upload a file for user: {}", authentication.getName());
        try {
            String email = authentication.getName(); // Get the authenticated user's email
            FileDTO fileDTO = fileService.store(file, email); // Store the uploaded file
            logger.info("File uploaded successfully: {}", fileDTO);
            return ResponseEntity.status(HttpStatus.OK).body(fileDTO); // Return success response
        } catch (Exception e) {
            logger.error("Error uploading file for user {}: {}", authentication.getName(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null); // Return error response
        }
    }

    /**
     * Endpoint for retrieving a list of files uploaded by the authenticated user.
     *
     * @param authentication the authentication object containing user details
     * @return ResponseEntity containing a list of FileDTOs for the user
     */
    @GetMapping
    public ResponseEntity<List<FileDTO>> getListFiles(Authentication authentication) {
        logger.info("Fetching list of files for user: {}", authentication.getName());
        String email = authentication.getName(); // Get the authenticated user's email
        List<FileDTO> files = fileService.getFilesByUser(email); // Retrieve user's files
        
        logger.info("Retrieved {} files for user: {}", files.size(), authentication.getName());
        return ResponseEntity.ok(files); // Return the list of files
    }

    /**
     * Endpoint for retrieving a file by its ID.
     *
     * @param id the ID of the file to retrieve
     * @return ResponseEntity containing the file data and HTTP headers
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable("id") String id) {
        logger.info("Attempting to retrieve file with ID: {}", id);
        try {
            File file = fileService.getFileById(id); // Retrieve the file by ID
            byte[] fileData = file.getData(); // Get the file data
            
            // Prepare the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getType())); // Set content type
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(file.getName()).build()); // Set file name for download
            
            logger.info("File retrieved successfully: {}", file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData); // Return file data
        } catch (Exception e) {
            logger.error("Error retrieving file with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return not found response
        }
    }
}
