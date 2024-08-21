package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.bilimili.video.clients.UserClient;
import com.bilimili.video.dao.Report;
import com.bilimili.video.dto.UserDTO;
import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.mapper.ReportMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.ReportService;
import com.bilimili.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserClient userClient;

    @Override
    public Report addReport(Report reportDTO) {
        Integer vid = reportDTO.getVid();
        VideoDTO videoDTO = videoService.getVideoWithDataByVid(vid);
        String sid = reportDTO.getSid();
        String authSid = reportDTO.getAuthSid();
        UserDTO userDTO = userClient.getUserDTOBySid(sid);
        UserDTO authUserDTO = userClient.getUserDTOBySid(authSid);
        Report report = new Report(
                null,
                vid,
                videoDTO.getTitle(),
                videoDTO.getVideoUrl(),
                reportDTO.getReason(),
                0,
                sid,
                userDTO.getName(),
                new Date(),
                authSid,
                authUserDTO.getName(),
                0,
                null);
        reportMapper.insert(report);
        return report;
    }

    @Override
    public String deleteReportByRid(Integer rid) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid", rid);
        reportMapper.delete(queryWrapper);
        return "删除成功";
    }

    @Override
    public CustomResponse updateReprotStatusByRid(Integer rid, Integer status) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("status", status);
        reportMapper.update(updateWrapper);
        customResponse.setMessage("修改status成功");
        return customResponse;
    }

    @Override
    public List<Report> getAllReports() {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        return reportMapper.selectList(queryWrapper);
    }

    @Override
    public List<Report> getReportByVid(Integer vid) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid", vid);
        return reportMapper.selectList(queryWrapper);
    }

    @Override
    public List<Report> getReportByStatus(Integer status) {
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return reportMapper.selectList(queryWrapper);
    }

    @Override
    public void appealReport(Integer vid) {
        UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("vid", vid).eq("appeal", 0).eq("status", 1);
    }

    @Override
    public CustomResponse updateReprotAppealByRid(Integer rid, Integer appeal) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("appeal", appeal);
        reportMapper.update(updateWrapper);
        customResponse.setMessage("修改appeal成功");
        return customResponse;
    }

    @Override
    public CustomResponse updateReprotAppealReasonByRid(Integer rid, Integer appealReason) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("appeal_reason", appealReason);
        reportMapper.update(updateWrapper);
        customResponse.setMessage("修改appeal_reason成功");
        return customResponse;
    }
}
