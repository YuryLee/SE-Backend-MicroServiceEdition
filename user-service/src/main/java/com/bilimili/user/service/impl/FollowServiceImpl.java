package com.bilimili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.user.dao.Follow;
import com.bilimili.user.dto.UserDTO;
import com.bilimili.user.mapper.FollowMapper;
import com.bilimili.user.response.CustomResponse;
import com.bilimili.user.service.FollowService;
import com.bilimili.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserService userService;

    @Override
    public CustomResponse addOrCancelFollow(String sidFrom, String sidTo) {
        CustomResponse customResponse = new CustomResponse();
        if (isFollowed(sidFrom, sidTo)) {
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sid_from", sidFrom).eq("sid_to", sidTo);
            followMapper.delete(queryWrapper);
            userService.updateStats(sidFrom, "follows_count", false, 1);
            userService.updateStats(sidTo, "fans_count", false, 1);
            customResponse.setMessage("取消关注");
        } else {
            Follow follow = new Follow(null, sidFrom, sidTo, new Date());
            followMapper.insert(follow);
            userService.updateStats(sidFrom, "follows_count", true, 1);
            userService.updateStats(sidTo, "fans_count", true, 1);
            customResponse.setMessage("关注");
        }
        return customResponse;
    }

    @Override
    public List<UserDTO> getFans(String sid) {
        List<UserDTO> userDTOList = new ArrayList<>();
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid_to", sid);
        List<Follow> followList = followMapper.selectList(queryWrapper);
        for (Follow follow : followList) {
            userDTOList.add(userService.getUserBySid(follow.getSidFrom()));
        }
        return userDTOList;
    }

    @Override
    public Boolean isFollowed(String sidFrom, String sidTo) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid_from", sidFrom).eq("sid_to", sidTo);
        Follow follow = followMapper.selectOne(queryWrapper);
        return (follow != null);
    }

    @Override
    public List<UserDTO> getFollows(String sid) {
        List<UserDTO> userDTOList = new ArrayList<>();
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid_from", sid);
        List<Follow> followList = followMapper.selectList(queryWrapper);
        for (Follow follow : followList) {
            userDTOList.add(userService.getUserBySid(follow.getSidTo()));
        }
        return userDTOList;
    }

}
