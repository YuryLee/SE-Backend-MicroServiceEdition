package com.bilimili.specol.controller;

import com.bilimili.specol.dao.ZhuanlanReport;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanReportService;
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
class ZhuanlanReportControllerTest {

    @Mock
    private ZhuanlanReportService zhuanlanReportService;

    @InjectMocks
    private ZhuanlanReportController zhuanlanReportController;

    public ZhuanlanReportControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReportPositive() {
        ZhuanlanReport reportDTO = new ZhuanlanReport();
        ZhuanlanReport expectedReport = new ZhuanlanReport();
        CustomResponse expectedResponse = new CustomResponse();
        expectedResponse.setData(expectedReport);

        when(zhuanlanReportService.addReport(reportDTO)).thenReturn(expectedReport);

        CustomResponse actualResponse = zhuanlanReportController.addReport(reportDTO);

        assertNotNull(actualResponse);
        assertSame(expectedReport, actualResponse.getData());
        verify(zhuanlanReportService, times(1)).addReport(reportDTO);
    }

    @Test
    void testAddReportNegative() {
        ZhuanlanReport reportDTO = new ZhuanlanReport();
        when(zhuanlanReportService.addReport(reportDTO)).thenReturn(null);
        CustomResponse actualResponse = zhuanlanReportController.addReport(reportDTO);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(zhuanlanReportService, times(1)).addReport(reportDTO);
    }

    @Test
    void testDeleteReportByRidPositive() {
        Integer rid = 1;
        String expectedMessage = "删除成功";
        when(zhuanlanReportService.deleteReportByRid(rid)).thenReturn(expectedMessage);
        CustomResponse actualResponse = zhuanlanReportController.deleteReportByRid(rid);
        assertNotNull(actualResponse);
        assertEquals(expectedMessage, actualResponse.getMessage());
        verify(zhuanlanReportService, times(1)).deleteReportByRid(rid);
    }

    @Test
    void testDeleteReportByRidNegative() {
        Integer rid = 1;
        when(zhuanlanReportService.deleteReportByRid(rid)).thenReturn(null);
        CustomResponse actualResponse = zhuanlanReportController.deleteReportByRid(rid);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(zhuanlanReportService, times(1)).deleteReportByRid(rid);
    }

    @Test
    void testUpdateReportStatusByRidPositive() {
        Integer rid = 1;
        Integer status = 2;
        CustomResponse expectedResponse = new CustomResponse();

        when(zhuanlanReportService.updateReprotStatusByRid(rid, status)).thenReturn(expectedResponse);

        CustomResponse actualResponse = zhuanlanReportController.updateReportStatusByRid(rid, status);

        assertSame(expectedResponse, actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotStatusByRid(rid, status);
    }

    @Test
    void testUpdateReportStatusByRidNegative() {
        Integer rid = 1;
        Integer status = 2;

        when(zhuanlanReportService.updateReprotStatusByRid(rid, status)).thenReturn(null);

        CustomResponse actualResponse = zhuanlanReportController.updateReportStatusByRid(rid, status);

        assertNull(actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotStatusByRid(rid, status);
    }

    @Test
    void testGetAllReportsPositive() {
        List<ZhuanlanReport> expectedReportList = new ArrayList<>();
        when(zhuanlanReportService.getAllReports()).thenReturn(expectedReportList);

        CustomResponse actualResponse = zhuanlanReportController.getAllReports();
        assertNotNull(actualResponse);
        assertSame(expectedReportList, actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getAllReports();
    }

    @Test
    void testGetAllReportsNegative() {
        when(zhuanlanReportService.getAllReports()).thenReturn(null);
        CustomResponse actualResponse = zhuanlanReportController.getAllReports();
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getAllReports();
    }

    @Test
    void testGetReportByVidPositive() {
        Integer zid = 1;
        List<ZhuanlanReport> expectedReportList = new ArrayList<>();

        when(zhuanlanReportService.getReportByVid(zid)).thenReturn(expectedReportList);

        CustomResponse actualResponse = zhuanlanReportController.getReportByVid(zid);

        assertNotNull(actualResponse);
        assertSame(expectedReportList, actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getReportByVid(zid);
    }

    @Test
    void testGetReportByVidNegative() {
        Integer zid = 1;
        when(zhuanlanReportService.getReportByVid(zid)).thenReturn(null);
        CustomResponse actualResponse = zhuanlanReportController.getReportByVid(zid);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getReportByVid(zid);
    }

    @Test
    void testGetReportByStatusPositive() {
        Integer status = 1;
        List<ZhuanlanReport> expectedReportList = new ArrayList<>();

        when(zhuanlanReportService.getReportByStatus(status)).thenReturn(expectedReportList);

        CustomResponse actualResponse = zhuanlanReportController.getReportByStatus(status);

        assertNotNull(actualResponse);
        assertSame(expectedReportList, actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getReportByStatus(status);
    }

    @Test
    void testGetReportByStatusNegative() {
        Integer status = 1;

        when(zhuanlanReportService.getReportByStatus(status)).thenReturn(null);

        CustomResponse actualResponse = zhuanlanReportController.getReportByStatus(status);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(zhuanlanReportService, times(1)).getReportByStatus(status);
    }

    @Test
    void testUpdateAppealPositive() {
        Integer rid = 1;
        Integer appeal = 2;
        CustomResponse expectedResponse = new CustomResponse();

        when(zhuanlanReportService.updateReprotAppealByRid(rid, appeal)).thenReturn(expectedResponse);

        CustomResponse actualResponse = zhuanlanReportController.updateAppeal(rid, appeal);

        assertSame(expectedResponse, actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotAppealByRid(rid, appeal);
    }

    @Test
    void testUpdateAppealNegative() {
        Integer rid = 1;
        Integer appeal = 2;

        when(zhuanlanReportService.updateReprotAppealByRid(rid, appeal)).thenReturn(null);

        CustomResponse actualResponse = zhuanlanReportController.updateAppeal(rid, appeal);

        assertNull(actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotAppealByRid(rid, appeal);
    }

    @Test
    void testUpdateAppealReasonPositive() {
        Integer rid = 1;
        Integer appealReason = 3;
        CustomResponse expectedResponse = new CustomResponse();

        when(zhuanlanReportService.updateReprotAppealReasonByRid(rid, appealReason)).thenReturn(expectedResponse);

        CustomResponse actualResponse = zhuanlanReportController.updateAppealReason(rid, appealReason);

        assertSame(expectedResponse, actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotAppealReasonByRid(rid, appealReason);
    }

    @Test
    void testUpdateAppealReasonNegative() {
        Integer rid = 1;
        Integer appealReason = 3;

        when(zhuanlanReportService.updateReprotAppealReasonByRid(rid, appealReason)).thenReturn(null);

        CustomResponse actualResponse = zhuanlanReportController.updateAppealReason(rid, appealReason);

        assertNull(actualResponse);
        verify(zhuanlanReportService, times(1)).updateReprotAppealReasonByRid(rid, appealReason);
    }
}
