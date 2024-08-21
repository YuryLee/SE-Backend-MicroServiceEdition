package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.video.dao.Danmu;
import com.bilimili.video.dao.Video;
import com.bilimili.video.mapper.DanmuMapper;
import com.bilimili.video.mapper.VideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.DanmuService;
import com.bilimili.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DanmuServiceImpl implements DanmuService {

    @Autowired
    private DanmuMapper danmuMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoService videoService;


    /**
     * 根据弹幕ID集合查询弹幕列表
     * @param vid 弹幕ID集合
     * @return  弹幕列表
     */
    @Override
    public List<Danmu> getDanmuListByVid(Integer vid) {
        QueryWrapper<Danmu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid", vid);
        return danmuMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public CustomResponse deleteDanmu(Integer id, String sid) {
        CustomResponse customResponse = new CustomResponse();
        QueryWrapper<Danmu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Danmu danmu = danmuMapper.selectOne(queryWrapper);
        if (danmu == null) {
            customResponse.setCode(404);
            customResponse.setMessage("弹幕不存在");
            return customResponse;
        }
        // 判断该用户是否有权限删除这条评论
        Video video = videoMapper.selectById(danmu.getVid());
        if (Objects.equals(danmu.getSid(), sid) || Objects.equals(video.getSid(), sid)) {
            // 删除弹幕
            QueryWrapper<Danmu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", id);
            danmuMapper.delete(queryWrapper1);
            videoService.updateStats(danmu.getVid(), "danmu", false, 1);
        } else {
            customResponse.setCode(403);
            customResponse.setMessage("你无权删除该条评论");
        }
        return customResponse;
    }

    @Override
    public CustomResponse sendDanmu(Danmu danmu) {
        CustomResponse customResponse = new CustomResponse();
        danmuMapper.insert(danmu);
        customResponse.setMessage("发送成功");
        customResponse.setData(danmu);

        videoService.updateStats(danmu.getVid(),"danmu", true, 1);

        return customResponse;
    }
}
