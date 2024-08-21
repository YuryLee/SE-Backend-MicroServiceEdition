package com.bilimili.video.service;

import com.bilimili.video.dao.Report;
import com.bilimili.video.response.CustomResponse;

import java.util.List;

public interface ReportService {
    Report addReport(Report reportDTO);

    String deleteReportByRid(Integer rid);

    CustomResponse updateReprotStatusByRid(Integer rid, Integer status);

    List<Report> getAllReports();

    List<Report> getReportByVid(Integer vid);

    List<Report> getReportByStatus(Integer status);

    void appealReport(Integer vid);

    CustomResponse updateReprotAppealByRid(Integer rid, Integer appeal);

    CustomResponse updateReprotAppealReasonByRid(Integer rid, Integer appealReason);
}
