package com.bilimili.video.controller;

import com.bilimili.video.converter.VideoConverter;
import com.bilimili.video.dao.*;
import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import com.bilimili.video.service.VideoReview;
import com.bilimili.video.service.impl.VideoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VideoControllerTest {

    @InjectMocks
    private VideoController videoController;

    @Mock
    private VideoServiceImpl videoServiceImpl;

    @Mock
    private VideoReview videoReview;

    @Mock
    private VideoConverter videoConverter;

    @Mock
    private UserVideoService userVideoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetVideoByVidPositive() {
        VideoDTO videoDTO = new VideoDTO();
        when(videoServiceImpl.getVideoWithDataByVid(anyInt())).thenReturn(videoDTO);
        CustomResponse response = videoController.getVideoByVid(123);
        assertNotNull(response);
        assertSame(videoDTO, response.getData());
        verify(videoServiceImpl, times(1)).getVideoWithDataByVid(anyInt());
    }

    @Test
    void testGetVideoByVidNegative() {
        when(videoServiceImpl.getVideoWithDataByVid(anyInt())).thenReturn(null);
        CustomResponse response = videoController.getVideoByVid(123);
        assertNotNull(response);
        assertEquals(404, response.getCode());
        assertEquals("没找到视频", response.getMessage());
        verify(videoServiceImpl, times(1)).getVideoWithDataByVid(anyInt());
    }

    @Test
    void testGetAllVideosPositive() {
        Map<String, Object> videoMap = new HashMap<>();
        when(videoServiceImpl.getAllVideos(anyInt())).thenReturn(videoMap);
        CustomResponse response = videoController.getAllVideos(1);
        assertNotNull(response);
        assertSame(videoMap, response.getData());
        verify(videoServiceImpl, times(1)).getAllVideos(anyInt());
    }

    @Test
    void testGetAllVideosNegative() {
        when(videoServiceImpl.getAllVideos(anyInt())).thenReturn(null);
        CustomResponse response = videoController.getAllVideos(1);
        assertNotNull(response);
        assertEquals(404, response.getCode());
        assertEquals("没找到视频", response.getMessage());
    }

    @Test
    void testUpdateVideoStatusPositive() {
        CustomResponse mockResponse = new CustomResponse();
        try {
            when(videoReview.updateVideoStatus(anyInt(), anyInt(), anyString())).thenReturn(mockResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response = videoController.updateVideoStatus(123, 1, "审核通过");
        assertSame(mockResponse, response);
        try {
            verify(videoReview, times(1)).updateVideoStatus(anyInt(), anyInt(), anyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateVideoStatusNegative() {
        try {
            when(videoReview.updateVideoStatus(anyInt(), anyInt(), anyString())).thenThrow(new RuntimeException("Test Exception"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomResponse response = videoController.updateVideoStatus(123, 1, "审核失败");

        assertNotNull(response);
        assertEquals(500, response.getCode());
        assertEquals("操作失败", response.getMessage());
        try {
            verify(videoReview, times(1)).updateVideoStatus(anyInt(), anyInt(), anyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetRandomVideosByCountPositive() {
        List<Video> videoList = new ArrayList<>();
        Video video = new Video();
        VideoDTO videoDTO = new VideoDTO();
        videoList.add(video);

        when(videoServiceImpl.getShuffleValidVideosByCount(anyInt())).thenReturn(videoList);
        when(videoConverter.Converter(any(Video.class))).thenReturn(videoDTO);

        CustomResponse response = videoController.getRandomVideos(5);

        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(videoDTO, ((List<?>) response.getData()).get(0));
        verify(videoServiceImpl, times(1)).getShuffleValidVideosByCount(anyInt());
        verify(videoConverter, times(1)).Converter(any(Video.class));
    }

    @Test
    void testGetRandomVideosByCountNegative() {
        when(videoServiceImpl.getShuffleValidVideosByCount(anyInt())).thenReturn(Collections.emptyList());
        CustomResponse response = videoController.getRandomVideos(5);
        assertNotNull(response);
        assertTrue(((List<?>) response.getData()).isEmpty());
        verify(videoServiceImpl, times(1)).getShuffleValidVideosByCount(anyInt());
        verify(videoConverter, never()).Converter(any(Video.class));
    }

    @Test
    void testGetRandomVideosByMcIdPositive() {
        List<Video> videoList = new ArrayList<>();
        Video video = new Video();
        VideoDTO videoDTO = new VideoDTO();
        videoList.add(video);

        when(videoServiceImpl.getShuffleValidVideosByMcidAndCount(anyInt(), anyString())).thenReturn(videoList);
        when(videoConverter.Converter(any(Video.class))).thenReturn(videoDTO);

        CustomResponse response = videoController.getRandomVideos(5, "mcId");

        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(videoDTO, ((List<?>) response.getData()).get(0));
        verify(videoServiceImpl, times(1)).getShuffleValidVideosByMcidAndCount(anyInt(), anyString());
        verify(videoConverter, times(1)).Converter(any(Video.class));
    }

    @Test
    void testGetRandomVideosByMcIdNegative() {
        when(videoServiceImpl.getShuffleValidVideosByMcidAndCount(anyInt(), anyString())).thenReturn(Collections.emptyList());
        CustomResponse response = videoController.getRandomVideos(5, "mcId");
        assertNotNull(response);
        assertTrue(((List<?>) response.getData()).isEmpty());
        verify(videoServiceImpl, times(1)).getShuffleValidVideosByMcidAndCount(anyInt(), anyString());
        verify(videoConverter, never()).Converter(any(Video.class));
    }

    @Test
    void testGetUserVideosBySidPositive() {
        Video video = new Video();
        VideoDTO videoDTO = new VideoDTO();
        List<Video> videoList = new ArrayList<>();
        videoList.add(video);

        when(videoServiceImpl.getVideosWithDataBySidOrderByDesc(anyString(), anyString())).thenReturn(videoList);
        when(videoConverter.Converter(any(Video.class))).thenReturn(videoDTO);

        CustomResponse response = videoController.getUserVideosBySid("sid1", 1);

        assertNotNull(response);
        assertFalse(((List<?>) response.getData()).isEmpty());
        assertSame(videoDTO, ((List<?>) response.getData()).get(0));
        verify(videoServiceImpl, times(1)).getVideosWithDataBySidOrderByDesc(anyString(), anyString());
        verify(videoConverter, times(1)).Converter(any(Video.class));
    }

    @Test
    void testGetUserVideosBySidNegative() {
        when(videoServiceImpl.getVideosWithDataBySidOrderByDesc(anyString(), anyString())).thenReturn(Collections.emptyList());
        CustomResponse response = videoController.getUserVideosBySid("sid1", 1);
        assertNotNull(response);
        assertTrue(((List<?>) response.getData()).isEmpty());
        verify(videoServiceImpl, times(1)).getVideosWithDataBySidOrderByDesc(anyString(), anyString());
        verify(videoConverter, never()).Converter(any(Video.class));
    }

    @Test
    void testGetUserPlayMoviesPositive() {
        // 正面测试：成功获取用户最近播放的视频
        List<Integer> videoList = Arrays.asList(1, 2, 3);
        when(userVideoService.getIdsBySid(anyString(), anyInt())).thenReturn(videoList);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        when(videoServiceImpl.getUserVideosByIdList(anyList())).thenReturn(videoDTOList);

        CustomResponse response = videoController.getUserPlayMovies("sid1", 3);

        assertNotNull(response);
        assertSame(videoDTOList, response.getData());
        verify(userVideoService, times(1)).getIdsBySid(anyString(), anyInt());
        verify(videoServiceImpl, times(1)).getUserVideosByIdList(anyList());
    }

    @Test
    void testGetUserPlayMoviesNegative() {
        when(userVideoService.getIdsBySid(anyString(), anyInt())).thenReturn(Collections.emptyList());
        when(videoServiceImpl.getUserVideosByIdList(Collections.emptyList())).thenReturn(Collections.emptyList());
        CustomResponse response = videoController.getUserPlayMovies("sid1", 3);
        assertNotNull(response);
        assertTrue(((List<?>) response.getData()).isEmpty());
        assertSame(Collections.emptyList(), response.getData());
        verify(videoServiceImpl, times(1)).getUserVideosByIdList(anyList());
        verify(userVideoService, times(1)).getIdsBySid(anyString(), anyInt());
    }
}
