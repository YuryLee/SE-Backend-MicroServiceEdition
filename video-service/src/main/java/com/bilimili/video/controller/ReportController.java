package com.bilimili.video.controller;


import com.bilimili.video.dao.Report;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */

@CrossOrigin
@RestController
@Tag(name = "视频举报api")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/video/report/add")
    @Operation(summary = "上传举报信息")
    public CustomResponse addReport(@RequestBody Report reportDTO) {
        CustomResponse customResponse = new CustomResponse();
        Report report = reportService.addReport(reportDTO);
        customResponse.setData(report);
        return customResponse;
    }

    @PostMapping("/video/report/delete")
    @Operation(summary = "删除举报信息")
    public CustomResponse deleteReportByRid(@RequestParam("rid") Integer rid) {
        CustomResponse customResponse = new CustomResponse();
        String msg = reportService.deleteReportByRid(rid);
        customResponse.setMessage(msg);
        return customResponse;
    }


    @PostMapping("/video/report/update-status")
    @Operation(summary = "更改举报信息状态")
    public CustomResponse updateReportStatusByRid(@RequestParam("rid") Integer rid,
                                            @RequestParam("status") Integer status) {
        return reportService.updateReprotStatusByRid(rid, status);
    }

    @GetMapping("/video/report/all")
    @Operation(summary = "获取所有举报信息")
    public CustomResponse getAllReports() {
        CustomResponse customResponse = new CustomResponse();
        List<Report> reportList = reportService.getAllReports();
        customResponse.setData(reportList);
        return customResponse;
    }

    @GetMapping("/video/report/vid/{vid}")
    @Operation(summary = "根据vid返回该视频的举报信息列表")
    public CustomResponse getReportByVid(@PathVariable Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        List<Report> reportList = reportService.getReportByVid(vid);
        customResponse.setData(reportList);
        return customResponse;
    }

    @GetMapping("/video/report/status/{status}")
    @Operation(summary = "根据status返回举报信息列表")
    public CustomResponse getReportByStatus(@PathVariable Integer status) {
        CustomResponse customResponse = new CustomResponse();
        List<Report> reportList = reportService.getReportByStatus(status);
        customResponse.setData(reportList);
        return customResponse;
    }

    @PostMapping("/video/report/update-appeal")
    @Operation(summary = "修改appeal")
    public CustomResponse updateAppeal(@RequestParam("rid") Integer rid,
                                       @RequestParam("appeal") Integer appeal) {
        return reportService.updateReprotAppealByRid(rid, appeal);
    }

    @PostMapping("/video/report/update-appeal-reason")
    @Operation(summary = "由rid修改appeal_reason")
    public CustomResponse updateAppealReason(@RequestParam("rid") Integer rid,
                                             @RequestParam("appeal_reason") Integer appealReason) {
        return reportService.updateReprotAppealReasonByRid(rid, appealReason);
    }
}
