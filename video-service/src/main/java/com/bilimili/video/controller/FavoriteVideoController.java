package com.bilimili.video.controller;


import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import com.bilimili.video.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "收藏夹api")
public class FavoriteVideoController {

    @Autowired
    private UserVideoService userVideoService;

    @Autowired
    private VideoService videoService;

    @PostMapping("/favorite/user/collect-all")
    @Operation(summary = "获取sid所有收藏的视频")
    public CustomResponse getUserCollectVideos(@RequestParam("sid") String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<Integer> list = userVideoService.getUserCollectVideoList(sid);
        customResponse.setData(videoService.getUserVideosByIdList(list));
        return customResponse;
    }

    @PostMapping("/favorite/user/collect-delete")
    @Operation(summary = "收藏夹删除视频，传入视频列表vid的字符串形式，逗号隔开")
    public CustomResponse deleteVideosFromFavorite(@RequestParam("sid") String sid, @RequestParam String vidListString) {
        CustomResponse customResponse = new CustomResponse();
        String[] vidStringList = vidListString.split(",");
        System.out.println(Arrays.toString(vidStringList));
        List<Integer> vidList = new ArrayList<>();
        for (String s: vidStringList) {
            System.out.println(s);
            vidList.add(Integer.parseInt(s));
        }
        System.out.println(vidList);
        customResponse = userVideoService.deleteVideosFromFavorite(sid, vidList);
        return customResponse;
    }

}
