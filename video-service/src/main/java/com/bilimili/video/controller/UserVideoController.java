package com.bilimili.video.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.video.dao.UserVideo;
import com.bilimili.video.mapper.UserVideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "用户与视频交互api")
public class UserVideoController {
    @Autowired
    private UserVideoService userVideoService;

    @Autowired
    private UserVideoMapper userVideoMapper;

    /**
     * 登录用户播放视频时更新播放次数，有30秒更新间隔（防止用户刷播放量）
     * @param vid   视频ID
     * @return  返回用户与该视频的交互数据
     */
    @PostMapping("/video/user/play")
    @Operation(summary = "sid用户播放vid视频，增加播放次数，设置播放时刻")
    public CustomResponse newPlayWithLoginUser(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(userVideoService.updatePlay(sid, vid));
        return customResponse;
    }

    /**
     * 点赞或点踩
     * @param vid   视频ID
     * @param isLove    赞还是踩 true赞 false踩
     * @param isSet     点还是取消 true点 false取消
     * @return 返回用户与该视频更新后的交互数据
     */
    @PostMapping("/video/user/love-or-not")
    @Operation(summary = "sid给vid点赞或点踩，isLove赞还是踩 true赞 false踩，isSet点还是取消 true点 false取消")
    public CustomResponse loveOrNot(@RequestParam("sid") String sid,
                                    @RequestParam("vid") Integer vid,
                                    @RequestParam("isLove") boolean isLove,
                                    @RequestParam("isSet") boolean isSet) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(userVideoService.setLoveOrUnlove(sid, vid, isLove, isSet));
        return customResponse;
    }

    @GetMapping("/video/user/is-loved")
    @Operation(summary = "sid是否点赞vid")
    private CustomResponse isLoved(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(userVideoService.isLoved(sid, vid));
        return customResponse;
    }

    /**
     * 收藏或取消某视频
     * @param vid   视频ID
     * @return  无数据返回
     */
    @PostMapping("/video/user/collect")
    @Operation(summary = "收藏或取消vid进sid收藏夹")
    public CustomResponse collectVideo(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse = userVideoService.addOrCancelFavoriteVideo(sid, vid);

        return customResponse;
    }

    @GetMapping("/video/user/is-collected")
    @Operation(summary = "sid是否收藏vid")
    public CustomResponse isCollect(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(userVideoService.isCollected(sid, vid));
        return customResponse;
    }

    // 微服务接口
    @GetMapping("/video-service/user/is-collected")
    @Operation(summary = "微服务接口：sid是否收藏vid")
    public Boolean isCollectService(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        return userVideoService.isCollected(sid, vid);
    }

    @GetMapping("/userVideo-service/user/video")
    @Operation(summary = "微服务接口：获得sid和vid的关系")
    public UserVideo getUserVideo(@RequestParam("sid") String sid, @RequestParam("vid") Integer vid) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("vid", vid);
        return userVideoMapper.selectOne(queryWrapper);
    }

    @PostMapping("/userVideo-service/insert/userVideo")
    @Operation(summary = "微服务接口：插入sid和vid的关系")
    public void insertUserVideo(@RequestBody UserVideo userVideo) {
        userVideoMapper.insert(userVideo);
    }

    @PostMapping("/userVideo-service/update/userVideo")
    @Operation(summary = "微服务接口：改变sid和vid的关系")
    public void updateUserVideoById(@RequestBody UserVideo userVideo) {
        userVideoMapper.updateById(userVideo);
    }

    @GetMapping("/userVideo-service/user/sid")
    @Operation(summary = "微服务接口：获得sid所有有关视频")
    public List<UserVideo> getUserVideoList(@RequestParam("sid") String sid) {
        QueryWrapper<UserVideo> userVideoQueryWrapper = new QueryWrapper<>();
        userVideoQueryWrapper.eq("sid", sid);
        return userVideoMapper.selectList(userVideoQueryWrapper);
    }
}
