package com.bilimili.user.service;

import com.bilimili.user.dao.DailyPlay;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
public interface DailyPlayService {
    void initData(String sid);

    List<DailyPlay> getDailyData(String sid);
}
