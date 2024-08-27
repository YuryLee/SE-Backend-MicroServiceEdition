package com.bilimili.video.controller;

import com.bilimili.video.dao.Danmu;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.DanmuService;
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
public class DanmuControllerTest {
    @InjectMocks
    private DanmuController danmuController;

    @Mock
    private DanmuService danmuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDanmuListPositive() {
        Integer vid = 100;
        Danmu danmu = new Danmu();
        List<Danmu> danmuList = new ArrayList<>();
        danmuList.add(danmu);
        when(danmuService.getDanmuListByVid(vid)).thenReturn(danmuList);
        CustomResponse customResponse = danmuController.getDanmuList(vid);
        assertNotNull(customResponse);
        assertEquals(1, ((List<?>) customResponse.getData()).size());
        assertSame(danmu, ((List<?>) customResponse.getData()).get(0));
        verify(danmuService, times(1)).getDanmuListByVid(vid);
    }

    @Test
    void testGetDanmuListNegative() {
        Integer vid = -100;
        List<Danmu> danmuList = new ArrayList<>();
        when(danmuService.getDanmuListByVid(vid)).thenReturn(danmuList);
        CustomResponse customResponse = danmuController.getDanmuList(vid);
        assertNotNull(customResponse);
        assertTrue(((List<?>) customResponse.getData()).isEmpty());
        verify(danmuService, times(1)).getDanmuListByVid(vid);
    }

    @Test
    public void testSendDanmuPositive() {
        CustomResponse expectedResponse = new CustomResponse();
        when(danmuService.sendDanmu(any(Danmu.class))).thenReturn(expectedResponse);
        CustomResponse actualResponse = danmuController.sendDanmu(1, "testSid", "scroll", "#FFFFFF", 10, 25, 1.5, true, "Test content", 5.0);
        assertSame(expectedResponse, actualResponse);
        verify(danmuService, times(1)).sendDanmu(any(Danmu.class));
    }

    @Test
    public void testSendDanmuNegative() {
        when(danmuService.sendDanmu(any(Danmu.class))).thenReturn(null);
        CustomResponse actualResponse = danmuController.sendDanmu(1, "testSid", "scroll", "#FFFFFF", 10, 25, 1.5, true, "Test content", 5.0);
        assertNull(actualResponse);
        verify(danmuService, times(1)).sendDanmu(any(Danmu.class));
    }

    @Test
    void testDelDanmuPositive() {
        Integer id = 100;
        String sid = "00000001";
        CustomResponse expectResponse = new CustomResponse();
        when(danmuService.deleteDanmu(id, sid)).thenReturn(expectResponse);
        CustomResponse actualResponse = danmuController.deleteDanmu(id, sid);
        assertSame(actualResponse, expectResponse);
        verify(danmuService, times(1)).deleteDanmu(id, sid);
    }

    @Test
    void testDelDanmuNegative() {
        Integer id = 100;
        String sid = "00000001";
        when(danmuService.deleteDanmu(id, sid)).thenReturn(null);
        CustomResponse actualResponse = danmuController.deleteDanmu(id, sid);
        assertNull(actualResponse);
        verify(danmuService, times(1)).deleteDanmu(id, sid);
    }
}