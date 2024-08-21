package com.bilimili.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bilimili.search.clients.SpecolClient;
import com.bilimili.search.clients.UserClient;
import com.bilimili.search.clients.VideoClient;
import com.bilimili.search.dao.User;
import com.bilimili.search.dao.Video;
import com.bilimili.search.dao.Zhuanlan;
import com.bilimili.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private VideoClient videoClient;

    @Autowired
    private SpecolClient specolClient;

    @Autowired
    private UserClient userClient;

    @Override
    public List<Video> searchVideos(String text) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.orderByDesc("play").eq("status", 1).like("title", text).or().like("descr", text);
        return videoClient.searchVideo(text);
    }

    @Override
    public List<User> searchUsers(String text) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.orderByDesc("fans_count").like("name", text).or().like("signature", text).or().like("sid", text);
        return userClient.searchUser(text);
    }

    @Override
    public List<Zhuanlan> searchZhuanlans(String text) {
        QueryWrapper<Zhuanlan> queryWrapper = new QueryWrapper<>();
        text = '%' + text + '%';
        queryWrapper.like("title", text).or().like("description", text);
        return specolClient.searchSpecol(text);
    }
}
