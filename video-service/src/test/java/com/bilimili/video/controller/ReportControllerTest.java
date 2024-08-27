package com.bilimili.video.controller;

import com.bilimili.video.dao.Report;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.ReportService;
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
public class ReportControllerTest {
    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReportPositive() {
        Report report = new Report();
        when(reportService.addReport(report)).thenReturn(report);
        CustomResponse actualResponse = reportController.addReport(report);
        assertSame(report, actualResponse.getData());
        verify(reportService).addReport(report);
    }

    @Test
    void testAddReportNegative() {
        when(reportService.addReport(null)).thenReturn(null);
        CustomResponse actualResponse = reportController.addReport(null);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(reportService).addReport(null);
    }

    @Test
    void testDeleteReportByRidPositive() {
        Integer rid = 1;
        String msg = "message";
        when(reportService.deleteReportByRid(rid)).thenReturn(msg);
        CustomResponse actualResponse = reportController.deleteReportByRid(rid);
        assertNotNull(actualResponse);
        assertEquals(msg, actualResponse.getMessage());
        verify(reportService).deleteReportByRid(rid);
    }

    @Test
    void testDeleteReportByRidNegative() {
        Integer rid = -1;
        when(reportService.deleteReportByRid(rid)).thenReturn(null);
        CustomResponse actualResponse = reportController.deleteReportByRid(rid);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getData());
        verify(reportService).deleteReportByRid(rid);
    }

    @Test
    void testUpdateReportStatusByRidPositive() {
        Integer rid = 1;
        Integer status = 1;
        CustomResponse expectedResponse = new CustomResponse();
        when(reportService.updateReprotStatusByRid(rid, status)).thenReturn(expectedResponse);
        CustomResponse actualResponse = reportController.updateReportStatusByRid(rid, status);
        assertSame(expectedResponse, actualResponse);
        verify(reportService).updateReprotStatusByRid(rid, status);
    }

    @Test
    void testUpdateReportStatusByRidNegative() {
        Integer rid = -1;
        Integer status = -1;
        when(reportService.updateReprotStatusByRid(rid, status)).thenReturn(null);
        CustomResponse actualResponse = reportController.updateReportStatusByRid(rid, status);
        assertNull(actualResponse);
        verify(reportService, times(1)).updateReprotStatusByRid(rid, status);
    }

    @Test
    void testGetAllReportsPositive() {
        Report report = new Report();
        List<Report> reports = new ArrayList<>();
        reports.add(report);
        when(reportService.getAllReports()).thenReturn(reports);
        CustomResponse actualResponse = reportController.getAllReports();
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(report, ((List<?>) actualResponse.getData()).get(0));
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    void testGetAllReportsNegative() {
        List<Report> reports = new ArrayList<>();

        when(reportService.getAllReports()).thenReturn(reports);
        CustomResponse actualResponse = reportController.getAllReports();
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    void testGetReportByVidPositive() {
        Integer vid = 1;
        Report report = new Report();
        List<Report> reports = new ArrayList<>();
        reports.add(report);
        when(reportService.getReportByVid(vid)).thenReturn(reports);
        CustomResponse actualResponse = reportController.getReportByVid(vid);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(report, ((List<?>) actualResponse.getData()).get(0));
        verify(reportService, times(1)).getReportByVid(vid);
    }

    @Test
    void testGetReportByVidNegative() {
        Integer vid = -1;
        List<Report> reports = new ArrayList<>();
        when(reportService.getReportByVid(vid)).thenReturn(reports);
        CustomResponse actualResponse = reportController.getReportByVid(vid);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(reportService, times(1)).getReportByVid(vid);
    }

    @Test
    void testGetReportByStatusPositive() {
        Integer status = 1;
        Report report = new Report();
        List<Report> reports = new ArrayList<>();
        reports.add(report);
        when(reportService.getReportByStatus(status)).thenReturn(reports);
        CustomResponse actualResponse = reportController.getReportByStatus(status);
        assertNotNull(actualResponse);
        assertEquals(1, ((List<?>) actualResponse.getData()).size());
        assertSame(report, ((List<?>) actualResponse.getData()).get(0));
        verify(reportService, times(1)).getReportByStatus(status);
    }

    @Test
    void testGetReportByStatusNegative() {
        Integer status = -1;
        List<Report> reports = new ArrayList<>();
        when(reportService.getReportByStatus(status)).thenReturn(reports);
        CustomResponse actualResponse = reportController.getReportByStatus(status);
        assertNotNull(actualResponse);
        assertTrue(((List<?>) actualResponse.getData()).isEmpty());
        verify(reportService, times(1)).getReportByStatus(status);
    }

    @Test
    void testUpdateAppealPositive() {
        Integer rid = 1;
        Integer appeal = 1;
        CustomResponse expectedResponse = new CustomResponse();
        when(reportService.updateReprotAppealByRid(rid, appeal)).thenReturn(expectedResponse);
        CustomResponse actualResponse = reportController.updateAppeal(rid, appeal);
        assertSame(expectedResponse, actualResponse);
        verify(reportService, times(1)).updateReprotAppealByRid(rid, appeal);
    }

    @Test
    void testUpdateAppealNegative() {
        Integer rid = -1;
        Integer appeal = -1;
        when(reportService.updateReprotAppealByRid(rid, appeal)).thenReturn(null);
        CustomResponse actualResponse = reportController.updateAppeal(rid, appeal);
        assertNull(actualResponse);
        verify(reportService, times(1)).updateReprotAppealByRid(rid, appeal);
    }

    @Test
    void testUpdateAppealReasonPositive() {
        Integer rid = 1;
        Integer appeal = 1;
        CustomResponse expectedResponse = new CustomResponse();
        when(reportService.updateReprotAppealReasonByRid(rid, appeal)).thenReturn(expectedResponse);
        CustomResponse actualResponse = reportController.updateAppealReason(rid, appeal);
        assertSame(expectedResponse, actualResponse);
        verify(reportService, times(1)).updateReprotAppealReasonByRid(rid, appeal);
    }

    @Test
    void testUpdateAppealReasonNegative() {
        Integer rid = -1;
        Integer appeal = -1;
        when(reportService.updateReprotAppealReasonByRid(rid, appeal)).thenReturn(null);
        CustomResponse actualResponse = reportController.updateAppealReason(rid, appeal);
        assertNull(actualResponse);
        verify(reportService, times(1)).updateReprotAppealReasonByRid(rid, appeal);
    }
}
