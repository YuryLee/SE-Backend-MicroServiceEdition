package com.bilimili.user.service;

import com.bilimili.user.dao.DailyWatch;

import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
public interface DailyWatchService {
    void initData(String sid);

    List<DailyWatch> getDailyData(String sid);
}
