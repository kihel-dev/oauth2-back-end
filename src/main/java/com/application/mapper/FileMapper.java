package com.application.mapper;

import com.application.dto.FileDTO;
import com.application.model.File;

/**
 * Mapper interface for converting between {@link File} entities and {@link FileDTO} objects.
 * 
 * This interface provides methods for mapping File entities to FileDTO objects and vice versa,
 * facilitating data transfer between different layers of the application.
 */
public interface FileMapper {

    /**
     * Converts a {@link File} entity to a {@link FileDTO}.
     *
     * @param fileDB the {@link File} entity to convert
     * @return the corresponding {@link FileDTO} object, or null if the input fileDB is null
     */
    FileDTO toDTO(File fileDB);

    /**
     * Converts a {@link FileDTO} to a {@link File} entity.
     *
     * @param fileDTO the {@link FileDTO} object to convert
     * @return the corresponding {@link File} entity, or null if the input fileDTO is null
     */
    File toModel(FileDTO fileDTO);
}
