package com.bilimili.search.service;

import com.bilimili.search.dao.User;
import com.bilimili.search.dao.Video;
import com.bilimili.search.dao.Zhuanlan;

import java.util.List;

public interface SearchService {
    List<Video> searchVideos(String text);

    List<User> searchUsers(String text);

    List<Zhuanlan> searchZhuanlans(String text);
}
