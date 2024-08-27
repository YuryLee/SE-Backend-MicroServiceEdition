package com.bilimili.user.controller;

import com.bilimili.user.dao.DailyWatch;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.DailyWatchService;
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
public class DailyWatchControllerTest {
    @InjectMocks
    private DailyWatchController dailyWatchController;

    @Mock
    private DailyWatchService dailyWatchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getDailyDataPositive() {
        String sid = "00000001";
        DailyWatch dailyWatch = new DailyWatch();
        List<DailyWatch> dailyWatchList = new ArrayList<>();
        dailyWatchList.add(dailyWatch);
        when(dailyWatchService.getDailyData(sid)).thenReturn(dailyWatchList);
        CustomResponse response = dailyWatchController.getDailyData(sid);
        assertNotNull(response);
        assertEquals(1, ((List<?>) response.getData()).size());
        assertSame(dailyWatch, ((List<?>) response.getData()).get(0));
        verify(dailyWatchService, times(1)).getDailyData(sid);
    }

    @Test
    void getDailyDataNegative() {
        String sid = "00000002";
        List<DailyWatch> dailyWatchList = new ArrayList<>();
        when(dailyWatchService.getDailyData(sid)).thenReturn(dailyWatchList);
        CustomResponse response = dailyWatchController.getDailyData(sid);
        assertNotNull(response);
        assertTrue(((List<?>) response.getData()).isEmpty());
        verify(dailyWatchService, times(1)).getDailyData(sid);
    }
}
