package com.bilimili.video.controller;

import com.bilimili.video.dto.VideoUploadInfoDTO;
import com.bilimili.video.handler.NonStaticResourceHttpRequestHandler;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.VideoUploadService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VideoUploadControllerTest {
    @InjectMocks
    private VideoUploadController videoUploadController;

    @Mock
    private VideoUploadService videoUploadService;

    @Mock
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private final String ECS_BUCKET_PATH = "src/test/resources/com/bilimili/video/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(videoUploadController, "ECSBucketPath", ECS_BUCKET_PATH);
    }

    @Test
    void testVideoPreviewPositive() throws Exception {
        String videoName = "testVideo.mp4";
        Path filePath = Path.of(ECS_BUCKET_PATH + "video/" + videoName);
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(filePath)).thenReturn(true);
            filesMockedStatic.when(() -> Files.probeContentType(filePath)).thenReturn("video/mp4");
        }
        videoUploadController.videoPreview(request, response, videoName);
        verify(response, times(1)).setContentType("video/mp4");
        verify(request, times(1)).setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
        verify(nonStaticResourceHttpRequestHandler, times(1)).handleRequest(request, response);
    }

    @Test
    public void testVideoPreviewNegative() throws Exception {
        String videoName = "nonexistent.mp4";
        Path filePath = Path.of(ECS_BUCKET_PATH + "video/" + videoName);
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(filePath)).thenReturn(true);
            filesMockedStatic.when(() -> Files.probeContentType(filePath)).thenReturn("video/mp4");
            filesMockedStatic.when(() -> Files.exists(filePath)).thenReturn(false);
        }
        videoUploadController.videoPreview(request, response, videoName);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response, times(1)).setCharacterEncoding(StandardCharsets.UTF_8.toString());
        verify(nonStaticResourceHttpRequestHandler, never()).handleRequest(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void testUploadVideoPositive() {
        MultipartFile mockFile = mock(MultipartFile.class);
        CustomResponse expectedResponse = new CustomResponse(200, "上传成功", null);
        try {
            when(videoUploadService.uploadVideo(mockFile)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response;
        try {
            response = videoUploadController.uploadVideo(mockFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertSame(expectedResponse, response);
        try {
            verify(videoUploadService, times(1)).uploadVideo(mockFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUploadVideoNegative() {
        MultipartFile mockFile = mock(MultipartFile.class);
        try {
            when(videoUploadService.uploadVideo(mockFile)).thenThrow(new RuntimeException("Test Exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse response;
        try {
            response = videoUploadController.uploadVideo(mockFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("上传失败", response.getMessage());
    }

    @Test
    void testDownloadPositive() {
        String videoName = "testVideo.mp4";
        HttpServletResponse response = mock(HttpServletResponse.class);
        byte[] expectedBytes;
        byte[] actualBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ServletOutputStream servletOutputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) {
                    outputStream.write(b);
                }
            };
            try {
                when(response.getOutputStream()).thenReturn(servletOutputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            videoUploadController.download(videoName, response);
            try {
                verify(response, times(1)).getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            actualBytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filePath = ECS_BUCKET_PATH + "video/" + videoName;
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            expectedBytes = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertArrayEquals(expectedBytes, actualBytes);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDownloadNegative() {
        String imageName = "testVideo.mp4";
        HttpServletResponse response = mock(HttpServletResponse.class);
        try {
            when(response.getOutputStream()).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        videoUploadController.download(imageName, response);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddVideoPositive() {
        MultipartFile mockCover = mock(MultipartFile.class);
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", null, null, "videoUrl");
        CustomResponse expectedResponse = new CustomResponse(200, "添加成功", null);
        try {
            when(videoUploadService.addVideo(mockCover, videoUploadInfoDTO)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CustomResponse response = videoUploadController.addVideo(mockCover, 1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", "videoUrl");
        assertSame(expectedResponse, response);
        try {
            verify(videoUploadService, times(1)).addVideo(mockCover, videoUploadInfoDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddVideoNegative() {
        MultipartFile mockCover = mock(MultipartFile.class);
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", null, null, "videoUrl");

        try {
            when(videoUploadService.addVideo(mockCover, videoUploadInfoDTO)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response = videoUploadController.addVideo(mockCover, 1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", "videoUrl");
        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("封面上传失败", response.getMessage());
        try {
            verify(videoUploadService, times(1)).addVideo(mockCover, videoUploadInfoDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateVideoPositive() {
        MultipartFile mockCover = mock(MultipartFile.class);
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", null, null, "videoUrl");
        CustomResponse expectedResponse = new CustomResponse(200, "更新成功", null);

        try {
            when(videoUploadService.updateVideo(1, mockCover, videoUploadInfoDTO)).thenReturn(expectedResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response = videoUploadController.updateVideo(1, mockCover, 1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", "videoUrl");

        assertNotNull(response);
        assertSame(expectedResponse, response);
        try {
            verify(videoUploadService, times(1)).updateVideo(1, mockCover, videoUploadInfoDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateVideoNegative() {
        MultipartFile mockCover = mock(MultipartFile.class);
        VideoUploadInfoDTO videoUploadInfoDTO = new VideoUploadInfoDTO(1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", null, null, "videoUrl");

        try {
            when(videoUploadService.updateVideo(1, mockCover, videoUploadInfoDTO)).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response = videoUploadController.updateVideo(1, mockCover, 1, "sid", "title", 1, 1, 3.5, "mcid", "scid", "tags", "descr", "videoUrl");
        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("封面上传失败", response.getMessage());
        try {
            verify(videoUploadService, times(1)).updateVideo(1, mockCover, videoUploadInfoDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getVideoCoverPositive() {
        String coverName = "testCover.png";
        HttpServletResponse response = mock(HttpServletResponse.class);
        byte[] expectedBytes;
        byte[] actualBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ServletOutputStream servletOutputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) {
                    outputStream.write(b);
                }
            };
            try {
                when(response.getOutputStream()).thenReturn(servletOutputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            videoUploadController.getVideoCover(coverName, response);
            try {
                verify(response, times(1)).getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            actualBytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filePath = ECS_BUCKET_PATH + "cover/" + coverName;
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            expectedBytes = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertArrayEquals(expectedBytes, actualBytes);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getVideoCoverNegative() {
        String imageName = "testCover.png";
        HttpServletResponse response = mock(HttpServletResponse.class);
        try {
            when(response.getOutputStream()).thenThrow(new RuntimeException("Test exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        videoUploadController.getVideoCover(imageName, response);
        try {
            verify(response, times(1)).getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCancelUploadPositive() {
        String videoUrl = "https://example.com/video.mp4";
        CustomResponse expectedResponse = new CustomResponse(200, "取消成功", null);

        when(videoUploadService.cancelUpload(videoUrl)).thenReturn(expectedResponse);

        CustomResponse response = videoUploadController.cancelUpload(videoUrl);

        assertSame(expectedResponse, response);
        verify(videoUploadService, times(1)).cancelUpload(videoUrl);
    }

    @Test
    void testCancelUploadNegative() {
        String videoUrl = "https://example.com/video.mp4";
        CustomResponse expectedResponse = new CustomResponse(500, "取消失败", null);

        when(videoUploadService.cancelUpload(videoUrl)).thenReturn(expectedResponse);

        CustomResponse response = videoUploadController.cancelUpload(videoUrl);

        assertSame(expectedResponse, response);
        verify(videoUploadService, times(1)).cancelUpload(videoUrl);
    }
}