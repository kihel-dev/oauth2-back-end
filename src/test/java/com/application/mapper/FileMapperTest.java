package com.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.application.dto.FileDTO;
import com.application.mapper.impl.FileMapperImpl;
import com.application.model.File;
import com.application.model.User;
import com.application.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FileMapperTest {

    private FileMapperImpl fileMapper;

    @BeforeEach
    public void setUp() {
        // Mock the FileRepository, if necessary, or use a real one if needed
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        fileMapper = new FileMapperImpl(fileRepository);
    }

    @Test
    public void testToDTO_NullInput() {
        FileDTO result = fileMapper.toDTO(null);
        assertNull(result, "Expected null when input is null");
    }

    @Test
    public void testToDTO_ValidInput() {
        User user = new User("1", "test@example.com", "Test User", "http://example.com/pic.jpg");
        File file = new File("123", "example.txt", "text/plain", new byte[]{1, 2, 3}, user);
        FileDTO result = fileMapper.toDTO(file);

        assertEquals(file.getId(), result.id(), "ID should match");
        assertEquals(file.getName(), result.name(), "Name should match");
        assertEquals(file.getType(), result.type(), "Type should match");
    }

    @Test
    public void testToModel_NullInput() {
        File result = fileMapper.toModel(null);
        assertNull(result, "Expected null when input is null");
    }

    @Test
    public void testToModel_ValidInput() {
        FileDTO fileDTO = new FileDTO("123", "example.txt", "text/plain");
        File result = fileMapper.toModel(fileDTO);

        assertEquals(fileDTO.id(), result.getId(), "ID should match");
        assertEquals(fileDTO.name(), result.getName(), "Name should match");
        assertEquals(fileDTO.type(), result.getType(), "Type should match");
    }
}
