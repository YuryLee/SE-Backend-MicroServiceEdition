package com.bilimili.video.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.video.converter.VideoConverter;
import com.bilimili.video.dao.Video;
import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.mapper.VideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import com.bilimili.video.service.VideoReview;
import com.bilimili.video.service.impl.VideoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "视频api")
public class VideoController {

    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private VideoReview videoReview;

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private UserVideoService userVideoService;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 获取单条视频的信息，视频 status
     * @param vid   视频vid
     * @return  视频信息
     */
    @GetMapping("/video/vid/{vid}")
    @Operation(summary = "通过vid获取视频")
    public CustomResponse getVideoByVid(@PathVariable("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        VideoDTO videoDTO = videoService.getVideoWithDataByVid(vid);
        if (videoDTO == null) {
            customResponse.setCode(404);
            customResponse.setMessage("没找到视频");
            return customResponse;
        }
        customResponse.setData(videoDTO);
        return customResponse;
    }

    @GetMapping("/video/all/status/{status}")
    @Operation(summary = "获得所有状态为status的视频")
    public CustomResponse getAllVideos(@PathVariable("status") Integer status) {
        CustomResponse customResponse = new CustomResponse();
        Map<String, Object> map = videoService.getAllVideos(status);
        if (map == null) {
            customResponse.setCode(404);
            customResponse.setMessage("没找到视频");
            return customResponse;
        }
        customResponse.setData(map);
        return customResponse;
    }

    /**
     * 更新视频状态，包括过审、不通过、删除，其中审核相关需要管理员权限，删除可以是管理员或者投稿用户
     * @param vid 视频ID
     * @param status 要修改的状态，1通过 2不通过 3删除
     * @param advice 建议
     * @return 无data返回 仅返回响应
     */
    @PostMapping("/video/change/status")
    @Operation(summary = "修改视频状态，要修改的状态，1通过 2不通过 3删除，并提供建议")
    public CustomResponse updateVideoStatus(@RequestParam("vid") Integer vid,
                                            @RequestParam("status") Integer status,
                                            @RequestParam("advice") String advice) {
        try {
            return videoReview.updateVideoStatus(vid, status, advice);
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse(500, "操作失败", null);
        }
    }

    /**
     * 获得随机视频
     * @return  返回count条随机推荐视频
     */
    @GetMapping("/video/random/{count}")
    @Operation(summary = "获得随机count个status==1的视频")
    public CustomResponse getRandomVideos(@PathVariable Integer count) {
        CustomResponse customResponse = new CustomResponse();
        List<Video> videoList = videoService.getShuffleValidVideosByCount(count);
        // 随机打乱列表顺序
        Collections.shuffle(videoList);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOList.add(videoConverter.Converter(video));
        }
        customResponse.setData(videoDTOList);
        return customResponse;
    }

    /**
     * 获得随机视频
     * @return  返回count条随机推荐视频
     */
    @GetMapping("/video/mcid")
    @Operation(summary = "获得随机count个status==1且mcid有的视频")
    public CustomResponse getRandomVideos(@RequestParam("count") Integer count, @RequestParam("mcid") String mcid) {
        CustomResponse customResponse = new CustomResponse();
        List<Video> videoList = videoService.getShuffleValidVideosByMcidAndCount(count, mcid);
        // 随机打乱列表顺序
        Collections.shuffle(videoList);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOList.add(videoConverter.Converter(video));
        }
        customResponse.setData(videoDTOList);
        return customResponse;
    }


    /**
     * 获取用户视频投稿
     * @param sid   用户sid
     * @param rule  排序方式 1 投稿日期 2 播放量 3 点赞数
     * @return  视频信息列表
     */
    @GetMapping("/video/sid/{sid}/all")
    @Operation(summary = "获取sid用户的所有投稿，不包含删除的（status==3）视频")
    public CustomResponse getUserVideosBySid(@PathVariable("sid") String sid,
                                             @RequestParam(value = "rule", required = false) Integer rule) {
        CustomResponse customResponse = new CustomResponse();
        List<Video> videoList;
        switch (rule) {
            case 2 -> videoList = videoService.getVideosWithDataBySidOrderByDesc(sid, "play");
            case 3 -> videoList = videoService.getVideosWithDataBySidOrderByDesc(sid, "good");
            default ->
                    videoList = videoService.getVideosWithDataBySidOrderByDesc(sid, "upload_date");
        }
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOList.add(videoConverter.Converter(video));
        }
        customResponse.setData(videoDTOList);
        return customResponse;
    }

//    @GetMapping("/video/search")
//    @Operation(summary = "搜索title或descr与text相似的视频")
//    public CustomResponse searchVideos(@RequestParam String text) {
//        CustomResponse customResponse = new CustomResponse();
//        List<Video> videoList = videoService.searchVideos(text);
//        List<VideoDTO> videoDTOList = new ArrayList<>();
//        for (Video video: videoList) {
//            videoDTOList.add(videoConverter.Converter(video));
//        }
//        customResponse.setData(videoDTOList);
//        return customResponse;
//    }

    /**
     * 获取当前登录用户最近播放视频列表
     * @param quantity  查询数量
     * @return  视频信息列表
     */
    @GetMapping("/video/user-play")
    @Operation(summary = "获取当前登录用户最近播放视频列表，quantity查询数量")
    public CustomResponse getUserPlayMovies(@RequestParam("sid") String sid,
                                            @RequestParam("quantity") Integer quantity) {
        CustomResponse customResponse = new CustomResponse();

        List<Integer> list = userVideoService.getIdsBySid(sid, quantity);
        customResponse.setData(videoService.getUserVideosByIdList(list));
        return customResponse;
    }


    // 微服务接口
    @GetMapping("/video-service/vid/{vid}")
    @Operation(summary = "服务接口：通过vid获取视频")
    public VideoDTO getVideoWithDataByVid(@PathVariable("vid") Integer vid) {
        return videoService.getVideoWithDataByVid(vid);
    }

    @PostMapping("/video-service/updateStats")
    @Operation(summary = "服务接口：更改vid视频数据")
    public void updateStats(@RequestParam("vid") Integer vid, @RequestParam("column") String column,
                     @RequestParam("increase") boolean increase, @RequestParam("count") Integer count) {
        videoService.updateStats(vid, column, increase, count);
    }

    @GetMapping("/video-service/videoList/sid/{sid}")
    @Operation(summary = "服务接口：获取sid所有视频")
    public List<Video> getVideoListBySid(@PathVariable("sid") String sid) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("status", 1);
        return videoMapper.selectList(queryWrapper);
    }

    @GetMapping("/video-service/search/{text}")
    @Operation(summary = "服务接口：搜索text相关视频")
    public List<Video> searchVideo(@PathVariable String text) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.orderByDesc("play").eq("status", 1);
        String finalText = text;
        queryWrapper.and(query->query.like("title", finalText).or().like("descr", finalText));
        return videoMapper.selectList(queryWrapper);
    }
}
