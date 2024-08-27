package com.bilimili.video.controller;

import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import com.bilimili.video.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FavoriteVideoControllerTest {
    @InjectMocks
    private FavoriteVideoController favoriteVideoController;

    @Mock
    private UserVideoService userVideoService;

    @Mock
    private VideoService videoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserCollectVideosPositive() {
        String sid = "00000001";

        Integer video = 100;
        List<Integer> videoList = new ArrayList<>();
        videoList.add(video);

        VideoDTO videoDTO = new VideoDTO();
        List<VideoDTO> videoDTOList = new ArrayList<>();
        videoDTOList.add(videoDTO);

        when(userVideoService.getUserCollectVideoList(sid)).thenReturn(videoList);
        when(videoService.getUserVideosByIdList(videoList)).thenReturn(videoDTOList);
        CustomResponse actualResponse = favoriteVideoController.getUserCollectVideos(sid);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(videoDTO, ((List<?>) actualResponse.getData()).get(0));
        verify(userVideoService, times(1)).getUserCollectVideoList(sid);
        verify(videoService, times(1)).getUserVideosByIdList(videoList);
    }

    @Test
    void testGetUserCollectVideosNegative() {
        String sid = "00000001";

        List<Integer> videoList = new ArrayList<>();
        List<VideoDTO> videoDTOList = new ArrayList<>();

        when(userVideoService.getUserCollectVideoList(sid)).thenReturn(videoList);
        when(videoService.getUserVideosByIdList(videoList)).thenReturn(videoDTOList);

        CustomResponse actualResponse = favoriteVideoController.getUserCollectVideos(sid);

        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(userVideoService, times(1)).getUserCollectVideoList(sid);
        verify(videoService, times(1)).getUserVideosByIdList(videoList);
    }

    @Test
    public void testDeleteVideosFromFavoritePositive() {
        String sid = "testSid";
        String vidListString = "1,2,3";
        CustomResponse expectedResponse = new CustomResponse();

        when(userVideoService.deleteVideosFromFavorite(eq(sid), anyList())).thenReturn(expectedResponse);

        CustomResponse actualResponse = favoriteVideoController.deleteVideosFromFavorite(sid, vidListString);

        assertSame(expectedResponse, actualResponse);
        verify(userVideoService, times(1)).deleteVideosFromFavorite(anyString(), anyList());
    }

    @Test
    public void testDeleteVideosFromFavoriteNegative() {
        String sid = "testSid";
        String vidListString = "1,2,3";
        when(userVideoService.deleteVideosFromFavorite(eq(sid), anyList())).thenReturn(null);

        CustomResponse actualResponse = favoriteVideoController.deleteVideosFromFavorite(sid, vidListString);

        assertNull(actualResponse);
        verify(userVideoService, times(1)).deleteVideosFromFavorite(anyString(), anyList());
    }
}
