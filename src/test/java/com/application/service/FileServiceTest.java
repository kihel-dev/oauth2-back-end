package com.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.application.dto.FileDTO;
import com.application.mapper.FileMapper;
import com.application.model.File;
import com.application.model.User;
import com.application.repository.FileRepository;
import com.application.repository.UserRepository;
import com.application.service.FileService;
import com.application.service.impl.FileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStore_Success() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getBytes()).thenReturn("file content".getBytes());

        User user = new User("1", "user@example.com", "User", "http://example.com/profile.jpg");
        File fileDB = new File("1", "file.txt", "text/plain", "file content".getBytes(), user);
        FileDTO fileDTO = new FileDTO("1", "file.txt", "text/plain");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(fileRepository.save(any(File.class))).thenReturn(fileDB);
        when(fileMapper.toDTO(fileDB)).thenReturn(fileDTO);

        FileDTO result = fileService.store(multipartFile, "user@example.com");

        assertEquals(fileDTO, result);
        verify(fileRepository).save(any(File.class));
    }

    @Test
    public void testStore_UserNotFound() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getBytes()).thenReturn("file content".getBytes());

        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            fileService.store(multipartFile, "user@example.com");
        });

        assertEquals("User not found with email: user@example.com", thrown.getMessage());
    }

    @Test
    public void testGetFileById_Success() {
        File file = new File("1", "file.txt", "text/plain", "file content".getBytes(), null);
        when(fileRepository.findById("1")).thenReturn(Optional.of(file));

        File result = fileService.getFileById("1");

        assertEquals(file, result);
        verify(fileRepository).findById("1");
    }

    @Test
    public void testGetFileById_FileNotFound() {
        when(fileRepository.findById("1")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            fileService.getFileById("1");
        });

        assertEquals("File not found: 1", thrown.getMessage());
    }

    @Test
    public void testGetFilesByUser_Success() {
        User user = new User("1", "user@example.com", "User", "http://example.com/profile.jpg");
        File file = new File("1", "file.txt", "text/plain", "file content".getBytes(), user);
        FileDTO fileDTO = new FileDTO("1", "file.txt", "text/plain");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);
        when(fileRepository.findByUser(user)).thenReturn(Collections.singletonList(file));
        when(fileMapper.toDTO(file)).thenReturn(fileDTO);

        List<FileDTO> result = fileService.getFilesByUser("user@example.com");

        assertEquals(Collections.singletonList(fileDTO), result);
        verify(fileRepository).findByUser(user);
    }

    @Test
    public void testGetFilesByUser_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            fileService.getFilesByUser("user@example.com");
        });

        assertEquals("User not found with email: user@example.com", thrown.getMessage());
    }
}
