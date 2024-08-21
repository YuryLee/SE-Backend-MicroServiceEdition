package com.bilimili.specol.service;


import com.bilimili.specol.dao.ZhuanlanReport;
import com.bilimili.specol.response.CustomResponse;

import java.util.List;

public interface ZhuanlanReportService {
    ZhuanlanReport addReport(ZhuanlanReport reportDTO);

    String deleteReportByRid(Integer rid);

    CustomResponse updateReprotStatusByRid(Integer rid, Integer status);

    List<ZhuanlanReport> getAllReports();

    List<ZhuanlanReport> getReportByVid(Integer zid);

    List<ZhuanlanReport> getReportByStatus(Integer status);

    CustomResponse updateReprotAppealByRid(Integer rid, Integer appeal);

    CustomResponse updateReprotAppealReasonByRid(Integer rid, Integer appealReason);
}
