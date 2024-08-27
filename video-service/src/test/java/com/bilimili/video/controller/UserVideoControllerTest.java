package com.bilimili.video.controller;

import com.bilimili.video.dao.*;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserVideoControllerTest {
    @InjectMocks
    private UserVideoController userVideoController;

    @Mock
    private UserVideoService userVideoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNewPlayWithLoginUserPositive() {
        UserVideo userVideo = new UserVideo();
        when(userVideoService.updatePlay(anyString(), anyInt())).thenReturn(userVideo);
        CustomResponse response = userVideoController.newPlayWithLoginUser("sid1", 123);
        assertSame(userVideo, response.getData());
        verify(userVideoService, times(1)).updatePlay(anyString(), anyInt());
    }

    @Test
    void testNewPlayWithLoginUserNegative() {
        when(userVideoService.updatePlay(anyString(), anyInt())).thenReturn(null);
        CustomResponse response = userVideoController.newPlayWithLoginUser("sid1", 123);
        assertNotNull(response);
        assertNull(response.getData());
        verify(userVideoService, times(1)).updatePlay(anyString(), anyInt());
    }

    @Test
    void testLoveOrNotPositive() {
        UserVideo userVideo = new UserVideo();
        when(userVideoService.setLoveOrUnlove(anyString(), anyInt(), anyBoolean(), anyBoolean())).thenReturn(userVideo);
        CustomResponse response = userVideoController.loveOrNot("sid1", 123, true, true);
        assertNotNull(response);
        assertSame(userVideo, response.getData());
        verify(userVideoService, times(1)).setLoveOrUnlove(anyString(), anyInt(), anyBoolean(), anyBoolean());
    }

    @Test
    void testLoveOrNotNegative() {
        when(userVideoService.setLoveOrUnlove(anyString(), anyInt(), anyBoolean(), anyBoolean())).thenReturn(null);
        CustomResponse response = userVideoController.loveOrNot("sid1", 123, true, true);
        assertNotNull(response);
        assertNull(response.getData());
        verify(userVideoService, times(1)).setLoveOrUnlove(anyString(), anyInt(), anyBoolean(), anyBoolean());
    }

    @Test
    void testIsLovedPositive() {
        Boolean bool = true;
        when(userVideoService.isLoved(anyString(), anyInt())).thenReturn(bool);
        CustomResponse response = userVideoController.isLoved("sid1", 123);
        assertNotNull(response);
        assertTrue((Boolean) response.getData());
        verify(userVideoService, times(1)).isLoved(anyString(), anyInt());
    }

    @Test
    void testIsLovedNegative() {
        when(userVideoService.isLoved(anyString(), anyInt())).thenReturn(false);

        CustomResponse response = userVideoController.isLoved("sid1", 123);

        assertNotNull(response);
        assertFalse((Boolean) response.getData());
        verify(userVideoService, times(1)).isLoved(anyString(), anyInt());
    }

    @Test
    void testCollectVideoPositive() {
        CustomResponse mockResponse = new CustomResponse();
        when(userVideoService.addOrCancelFavoriteVideo(anyString(), anyInt())).thenReturn(mockResponse);
        CustomResponse response = userVideoController.collectVideo("sid1", 123);
        assertSame(mockResponse, response);
        verify(userVideoService, times(1)).addOrCancelFavoriteVideo(anyString(), anyInt());
    }

    @Test
    void testCollectVideoNegative() {
        when(userVideoService.addOrCancelFavoriteVideo(anyString(), anyInt())).thenReturn(null);
        CustomResponse response = userVideoController.collectVideo("sid1", 123);
        assertNull(response);
        verify(userVideoService, times(1)).addOrCancelFavoriteVideo(anyString(), anyInt());
    }

    @Test
    void testIsCollectPositive() {
        when(userVideoService.isCollected(anyString(), anyInt())).thenReturn(true);

        CustomResponse response = userVideoController.isCollect("sid1", 123);

        assertNotNull(response);
        assertTrue((Boolean) response.getData());
        verify(userVideoService, times(1)).isCollected(anyString(), anyInt());
    }

    @Test
    void testIsCollectNegative() {
        when(userVideoService.isCollected(anyString(), anyInt())).thenReturn(false);

        CustomResponse response = userVideoController.isCollect("sid1", 123);

        assertNotNull(response);
        assertFalse((Boolean) response.getData());
        verify(userVideoService, times(1)).isCollected(anyString(), anyInt());
    }
}
