package com.bilimili.specol.controller;

import com.bilimili.specol.dao.ZhuanlanReport;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanReportService;
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
@Tag(name = "专栏举报api")
public class ZhuanlanReportController {

    @Autowired
    private ZhuanlanReportService zhuanlanReportService;

    @PostMapping("/zhuanlan/report/add")
    @Operation(summary = "上传举报信息")
    public CustomResponse addReport(@RequestBody ZhuanlanReport reportDTO) {
        CustomResponse customResponse = new CustomResponse();
        ZhuanlanReport report = zhuanlanReportService.addReport(reportDTO);
        customResponse.setData(report);
        return customResponse;
    }

    @PostMapping("/zhuanlan/report/delete")
    @Operation(summary = "删除举报信息")
    public CustomResponse deleteReportByRid(@RequestParam("rid") Integer rid) {
        CustomResponse customResponse = new CustomResponse();
        String msg = zhuanlanReportService.deleteReportByRid(rid);
        customResponse.setMessage(msg);
        return customResponse;
    }


    @PostMapping("/zhuanlan/report/update-status")
    @Operation(summary = "更改举报信息状态")
    public CustomResponse updateReportStatusByRid(@RequestParam("rid") Integer rid,
                                            @RequestParam("status") Integer status) {
        return zhuanlanReportService.updateReprotStatusByRid(rid, status);
    }

    @GetMapping("/zhuanlan/report/zhuanlan/all")
    @Operation(summary = "获取所有举报信息")
    public CustomResponse getAllReports() {
        CustomResponse customResponse = new CustomResponse();
        List<ZhuanlanReport> reportList = zhuanlanReportService.getAllReports();
        customResponse.setData(reportList);
        return customResponse;
    }

    @GetMapping("/zhuanlan/report/zid/{zid}")
    @Operation(summary = "根据zid返回该专栏的举报信息列表")
    public CustomResponse getReportByVid(@PathVariable Integer zid) {
        CustomResponse customResponse = new CustomResponse();
        List<ZhuanlanReport> reportList = zhuanlanReportService.getReportByVid(zid);
        customResponse.setData(reportList);
        return customResponse;
    }

    @GetMapping("/zhuanlan/report/status/{status}")
    @Operation(summary = "根据status返回举报信息列表")
    public CustomResponse getReportByStatus(@PathVariable Integer status) {
        CustomResponse customResponse = new CustomResponse();
        List<ZhuanlanReport> reportList = zhuanlanReportService.getReportByStatus(status);
        customResponse.setData(reportList);
        return customResponse;
    }

    @PostMapping("/zhuanlan/report/update-appeal")
    @Operation(summary = "修改appeal")
    public CustomResponse updateAppeal(@RequestParam("rid") Integer rid,
                                       @RequestParam("appeal") Integer appeal) {
        return zhuanlanReportService.updateReprotAppealByRid(rid, appeal);
    }

    @PostMapping("/zhuanlan/report/update-appeal-reason")
    @Operation(summary = "由rid修改appeal_reason")
    public CustomResponse updateAppealReason(@RequestParam("rid") Integer rid,
                                             @RequestParam("appeal_reason") Integer appealReason) {
        return zhuanlanReportService.updateReprotAppealReasonByRid(rid, appealReason);
    }
}
