package com.bilimili.user.controller;

import com.bilimili.user.dao.DailyPlay;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.DailyPlayService;
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
public class DailyPlayControllerTest {
    @InjectMocks
    private DailyPlayController dailyPlayController;

    @Mock
    private DailyPlayService dailyPlayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDailyDataPositive() {
        String sid = "00000001";
        DailyPlay dailyPlay = new DailyPlay();
        List<DailyPlay> dailyPlayList = new ArrayList<>();
        dailyPlayList.add(dailyPlay);
        when(dailyPlayService.getDailyData(sid)).thenReturn(dailyPlayList);
        CustomResponse response = dailyPlayController.getDailyData(sid);
        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(dailyPlay, ((List<?>) response.getData()).get(0));
        verify(dailyPlayService, times(1)).getDailyData(sid);
    }

    @Test
    void getDailyDataNegative() {
        String sid = "00000002";
        List<DailyPlay> dailyPlayList = new ArrayList<>();
        when(dailyPlayService.getDailyData(sid)).thenReturn(dailyPlayList);
        CustomResponse response = dailyPlayController.getDailyData(sid);
        assertNotNull(response);
        assertSame(0, ((List<?>) response.getData()).size());
        verify(dailyPlayService, times(1)).getDailyData(sid);
    }
}
