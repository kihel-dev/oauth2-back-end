package com.application.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.application.dto.FileDTO;
import com.application.model.File;
import com.application.service.FileService;
import com.application.security.CustomUserDetails;
import com.application.security.JwtAuthenticationFilter;
import com.application.security.TokenProvider;
import com.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileControllerTest {

    @Mock
    private FileService fileService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a mock authentication
        UserDetails userDetails = new CustomUserDetails("user@example.com", "User Name", "profilePicUrl", Collections.emptyMap());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testUploadFile_Success() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        FileDTO fileDTO = new FileDTO("1", "file.txt", "text/plain");

        when(fileService.store(any(MultipartFile.class), anyString())).thenReturn(fileDTO);

        ResponseEntity<FileDTO> response = fileController.uploadFile(multipartFile, SecurityContextHolder.getContext().getAuthentication());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileDTO, response.getBody());
        verify(fileService).store(multipartFile, "user@example.com");
    }

    @Test
    public void testUploadFile_Failure() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(fileService.store(any(MultipartFile.class), anyString())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<FileDTO> response = fileController.uploadFile(multipartFile, SecurityContextHolder.getContext().getAuthentication());

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetListFiles_Success() {
        FileDTO fileDTO = new FileDTO("1", "file.txt", "text/plain");
        List<FileDTO> fileDTOList = Collections.singletonList(fileDTO);

        when(fileService.getFilesByUser(anyString())).thenReturn(fileDTOList);

        ResponseEntity<List<FileDTO>> response = fileController.getListFiles(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileDTOList, response.getBody());
    }

    @Test
    public void testGetFileById_Success() {
        File file = new File("1", "file.txt", "text/plain", "file content".getBytes(), null);
        FileDTO fileDTO = new FileDTO("1", "file.txt", "text/plain");

        when(fileService.getFileById("1")).thenReturn(file);

        ResponseEntity<byte[]> response = fileController.getFileById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(file.getData(), response.getBody());
        assertEquals(file.getType(), response.getHeaders().getContentType().toString());
        assertEquals(file.getName(), response.getHeaders().getContentDisposition().getFilename());
    }

    @Test
    public void testGetFileById_FileNotFound() {
        when(fileService.getFileById("1")).thenThrow(new RuntimeException("File not found"));

        ResponseEntity<byte[]> response = fileController.getFileById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
