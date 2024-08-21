package com.bilimili.search.controller;

import com.bilimili.search.converter.UserConverter;
import com.bilimili.search.converter.VideoConverter;
import com.bilimili.search.dao.User;
import com.bilimili.search.dao.Video;
import com.bilimili.search.dao.Zhuanlan;
import com.bilimili.search.dto.UserDTO;
import com.bilimili.search.dto.VideoDTO;
import com.bilimili.search.response.CustomResponse;
import com.bilimili.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "搜索api")
public class SearchController {

    @Autowired
    private VideoConverter videoConverter;

    @Autowired
    private SearchService searchService;

    @GetMapping("/user/search")
    @Operation(summary = "搜索sid或name或signature与text相似的用户")
    public CustomResponse searchUsers(@RequestParam String text) {
        CustomResponse customResponse = new CustomResponse();
        List<User> videoList = searchService.searchUsers(text);
        List<UserDTO> videoDTOList = new ArrayList<>();
        for (User user: videoList) {
            videoDTOList.add(UserConverter.convertUser(user));
        }
        customResponse.setData(videoDTOList);
        return customResponse;
    }

    @GetMapping("/video/search")
    @Operation(summary = "搜索title或descr与text相似的视频")
    public CustomResponse searchVideos(@RequestParam String text) {
        CustomResponse customResponse = new CustomResponse();
        List<Video> videoList = searchService.searchVideos(text);
        List<VideoDTO> videoDTOList = new ArrayList<>();
        for (Video video: videoList) {
            videoDTOList.add(videoConverter.Converter(video));
        }
        customResponse.setData(videoDTOList);
        return customResponse;
    }

    @GetMapping("/zhuanlan/search")
    @Operation(summary = "搜索title或descr与text相似的专栏")
    public CustomResponse searchZhuanlans(@RequestParam String text) {
        CustomResponse customResponse = new CustomResponse();
        List<Zhuanlan> zhuanlanList = searchService.searchZhuanlans(text);
        customResponse.setData(zhuanlanList);
        return customResponse;
    }
}
