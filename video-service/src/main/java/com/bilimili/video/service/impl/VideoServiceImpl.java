package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.video.converter.VideoConverter;
import com.bilimili.video.dao.Video;
import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.mapper.VideoMapper;
import com.bilimili.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Math.min;

/**
 * Description:
 *
 * @author Yury
 */
@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${ecs.bucket.path}")
    private String ECSBucketPath;

    @Value("${ecs.url}")
    private String ECSUrl;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoConverter videoConverter;

    /**
     * 根据vid查询单个视频信息，包含用户信息和分区信息
     *
     * @param vid 视频ID
     * @return 包含用户信息、分区信息、视频信息的map
     */
    @Override
    public VideoDTO getVideoWithDataByVid(Integer vid) {

        Video video;

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid", vid);
        video = videoMapper.selectOne(queryWrapper);

        // 多线程异步并行查询用户信息和分区信息并封装
        Video finalVideo = video;
//        CompletableFuture<Void> userFuture = CompletableFuture.runAsync(() -> {
//            map.put("user", userService.getUserById(finalVideo.getUid()));
//            map.put("stats", videoStatsService.getVideoStatsById(finalVideo.getVid()));
//        }, taskExecutor);
//        CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(() -> {
//            map.put("category", categoryService.getCategoryById(finalVideo.getMcId(), finalVideo.getScId()));
//        }, taskExecutor);
        // 使用join()等待userFuture和categoryFuture任务完成
//        userFuture.join();
//        categoryFuture.join();

        return videoConverter.Converter(video);
    }

    @Override
    public Map<String, Object> getAllVideos(Integer status) {
        Map<String, Object> map = new HashMap<>();
        List<Video> videoList = new ArrayList<>();

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        videoList = videoMapper.selectList(queryWrapper);
        int count = videoList.size();
        map.put("count", count);
        map.put("status", status);
        map.put("videoList", videoList);
        return map;
    }

    @Override
    public List<Video> getShuffleValidVideosByCount(Integer count) {
        List<Video> videoList = new ArrayList<>();

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        videoList = videoMapper.selectList(queryWrapper);
        // 随机打乱列表顺序
        Collections.shuffle(videoList);
        int len = videoList.size();

        return videoList.subList(0, min(count, len));
    }


    @Override
    public List<VideoDTO> getUserVideosByIdList(List<Integer> idList) {
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (Integer id : idList) {
            QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("vid", id);
            videoDTOList.add(videoConverter.Converter(videoMapper.selectOne(queryWrapper)));
        }
        return videoDTOList;
    }

    @Override
    public List<Video> getVideosWithDataBySidOrderByDesc(String sid, @Nullable String column) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).ne("status", 3).orderByDesc(column);

        return videoMapper.selectList(queryWrapper);
    }

    /**
     * 更新指定字段
     * @param vid   视频ID
     * @param column    对应数据库的列名
     * @param increase  是否增加，true则增加 false则减少
     * @param count 增减数量 一般是1，只有投币可以加2
     */
    @Override
    public void updateStats(Integer vid, String column, boolean increase, Integer count) {
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("vid", vid);
        if (increase) {
            updateWrapper.setSql(column + " = " + column + " + " + count);
        } else {
            // 更新后的字段不能小于0
            updateWrapper.setSql(column + " = CASE WHEN " + column + " - " + count + " < 0 THEN 0 ELSE " + column + " - " + count + " END");
        }
        videoMapper.update(null, updateWrapper);
    }

    /**
     * 同时更新点赞和点踩
     * @param vid   视频ID
     * @param addGood   是否点赞，true则good+1&bad-1，false则good-1&bad+1
     */
    @Override
    public void updateGoodAndBad(Integer vid, boolean addGood) {
        UpdateWrapper<Video> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("vid", vid);
        if (addGood) {
            updateWrapper.setSql("good = good + 1");
            updateWrapper.setSql("bad = CASE WHEN bad - 1 < 0 THEN 0 ELSE bad - 1 END");
        } else {
            updateWrapper.setSql("bad = bad + 1");
            updateWrapper.setSql("good = CASE WHEN good - 1 < 0 THEN 0 ELSE good - 1 END");
        }
        videoMapper.update(null, updateWrapper);
    }

    @Override
    public List<Video> getShuffleValidVideosByMcidAndCount(Integer count, String mcid) {
        List<Video> videoList = new ArrayList<>();

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).eq("mc_id", mcid);
        videoList = videoMapper.selectList(queryWrapper);
        // 随机打乱列表顺序
        Collections.shuffle(videoList);
        int len = videoList.size();

        return videoList.subList(0, min(count, len));
    }
}


