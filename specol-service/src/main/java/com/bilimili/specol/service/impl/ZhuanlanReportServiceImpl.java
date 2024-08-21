package com.bilimili.specol.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.specol.clients.UserClient;
import com.bilimili.specol.dao.Zhuanlan;
import com.bilimili.specol.dao.ZhuanlanReport;
import com.bilimili.specol.dto.UserDTO;
import com.bilimili.specol.mapper.ZhuanlanMapper;
import com.bilimili.specol.mapper.ZhuanlanReportMapper;
import com.bilimili.specol.response.CustomResponse;
import com.bilimili.specol.service.ZhuanlanReportService;
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
public class ZhuanlanReportServiceImpl implements ZhuanlanReportService {

    @Autowired
    private ZhuanlanReportMapper zhuanlanReportMapper;

    @Autowired
    private ZhuanlanMapper zhuanlanMapper;

    @Autowired
    private UserClient userClient;

    @Override
    public ZhuanlanReport addReport(ZhuanlanReport reportDTO) {
        Integer zid = reportDTO.getZid();
        Zhuanlan zhuanlan = zhuanlanMapper.selectById(zid);
        String sid = reportDTO.getSid();
        String authSid = reportDTO.getAuthSid();
        UserDTO userDTO = userClient.getUserDTOBySid(sid);
        UserDTO authUserDTO = userClient.getUserDTOBySid(authSid);
        ZhuanlanReport zhuanlanReport = new ZhuanlanReport(
                null,
                zid,
                zhuanlan.getTitle(),
                reportDTO.getReason(),
                0,
                sid,
                userDTO.getName(),
                new Date(),
                authSid,
                authUserDTO.getName(),
                0,
                null);
        zhuanlanReportMapper.insert(zhuanlanReport);
        return zhuanlanReport;
    }

    @Override
    public String deleteReportByRid(Integer rid) {
        QueryWrapper<ZhuanlanReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid", rid);
        zhuanlanReportMapper.delete(queryWrapper);
        return "删除成功";
    }

    @Override
    public CustomResponse updateReprotStatusByRid(Integer rid, Integer status) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<ZhuanlanReport> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("status", status);
        zhuanlanReportMapper.update(updateWrapper);
        customResponse.setMessage("修改status成功");
        return customResponse;
    }

    @Override
    public List<ZhuanlanReport> getAllReports() {
        QueryWrapper<ZhuanlanReport> queryWrapper = new QueryWrapper<>();
        return zhuanlanReportMapper.selectList(queryWrapper);
    }

    @Override
    public List<ZhuanlanReport> getReportByVid(Integer zid) {
        QueryWrapper<ZhuanlanReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("zid", zid);
        return zhuanlanReportMapper.selectList(queryWrapper);
    }

    @Override
    public List<ZhuanlanReport> getReportByStatus(Integer status) {
        QueryWrapper<ZhuanlanReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return zhuanlanReportMapper.selectList(queryWrapper);
    }


    @Override
    public CustomResponse updateReprotAppealByRid(Integer rid, Integer appeal) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<ZhuanlanReport> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("appeal", appeal);
        zhuanlanReportMapper.update(updateWrapper);
        customResponse.setMessage("修改appeal成功");
        return customResponse;
    }

    @Override
    public CustomResponse updateReprotAppealReasonByRid(Integer rid, Integer appealReason) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<ZhuanlanReport> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("rid", rid).set("appeal_reason", appealReason);
        zhuanlanReportMapper.update(updateWrapper);
        customResponse.setMessage("修改appeal_reason成功");
        return customResponse;
    }
}
