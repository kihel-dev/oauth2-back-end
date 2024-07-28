package com.application.mapper.impl;

import org.springframework.stereotype.Component;
import com.application.dto.FileDTO;
import com.application.mapper.FileMapper;
import com.application.model.File;
import com.application.repository.FileRepository;

/**
 * Implementation of the {@link FileMapper} interface for converting between 
 * {@link File} entities and {@link FileDTO} objects.
 * 
 * This class provides manual mapping logic to transform File entities to 
 * FileDTOs and vice versa.
 */
@Component
public class FileMapperImpl implements FileMapper {

    private final FileRepository fileRepository;

    /**
     * Constructor for FileMapperImpl.
     *
     * @param fileRepository the repository used for file operations; can be used for additional logic if needed
     */
    public FileMapperImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Converts a {@link File} entity to a {@link FileDTO}.
     *
     * @param fileDB the {@link File} entity to convert
     * @return the corresponding {@link FileDTO} object, or null if the input fileDB is null
     */
    @Override
    public FileDTO toDTO(File fileDB) {
        if (fileDB == null) {
            return null; // Return null if the input fileDB is null
        }

        return new FileDTO(fileDB.getId(), fileDB.getName(), fileDB.getType()); // Create and return the FileDTO
    }

    /**
     * Converts a {@link FileDTO} to a {@link File} entity.
     *
     * @param fileDTO the {@link FileDTO} object to convert
     * @return the corresponding {@link File} entity, or null if the input fileDTO is null
     */
    @Override
    public File toModel(FileDTO fileDTO) {
        if (fileDTO == null) {
            return null; // Return null if the input fileDTO is null
        }
        
        return new File(fileDTO.id(), fileDTO.name(), fileDTO.type()); // Create and return the File entity
    }
}
