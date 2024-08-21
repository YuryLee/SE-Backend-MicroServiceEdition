package com.bilimili.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.user.dao.Follow;
import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@CrossOrigin
@RestController
@Tag(name = "关注api")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/follow/add")
    @Operation(summary = "添加或取消关注")
    public CustomResponse addOrCancelFollow(
            @RequestParam("sid_from") String sidFrom,
            @RequestParam("sid_to") String sidTo) {

        return followService.addOrCancelFollow(sidFrom, sidTo);
    }

    @GetMapping("/follow/is-followed")
    @Operation(summary = "是否关注")
    public CustomResponse isFollowed(@RequestParam("sid_from") String sidFrom,
                                     @RequestParam("sid_to") String sidTo) {
        CustomResponse customResponse = new CustomResponse();

        customResponse.setData(followService.isFollowed(sidFrom, sidTo));
        return customResponse;
    }

    @GetMapping("/follow/fans")
    @Operation(summary = "获得sid粉丝列表")
    public CustomResponse getFans(@RequestParam("sid") String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<UserDTO> userDTOList = followService.getFans(sid);

        customResponse.setData(userDTOList);
        return customResponse;
    }

    @GetMapping("/follow/follows")
    @Operation(summary = "获得sid关注列表")
    public CustomResponse getFollows(@RequestParam("sid") String sid) {
        CustomResponse customResponse = new CustomResponse();
        List<UserDTO> userDTOList = followService.getFollows(sid);

        customResponse.setData(userDTOList);
        return customResponse;
    }

    // 微服务接口
    @GetMapping("follow-service/follows")
    @Operation(summary = "服务接口：获得sid关注列表")
    public List<UserDTO> getFollowsService(@RequestParam("sid") String sid) {
        return followService.getFollows(sid);
    }

    @GetMapping("/follow-service/fans")
    @Operation(summary = "服务接口：获得sid粉丝列表")
    public List<UserDTO> getFansService(@RequestParam("sid") String sid) {
        return followService.getFans(sid);
    }
}
