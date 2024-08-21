package com.bilimili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.bilimili.user.clients.MsgClient;
import com.bilimili.user.dao.DailyPlay;
import com.bilimili.user.dao.User;
import com.bilimili.user.mapper.DailyPlayMapper;
import com.bilimili.user.mapper.UserMapper;
import com.bilimili.user.service.DailyPlayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yury
 */
@Service
public class DailyPlayServiceImpl implements DailyPlayService {

    @Autowired
    private MsgClient msgClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DailyPlayMapper dailyPlayMapper;


    // 每天午夜12点执行
    //@Scheduled(cron = "0 0 0 * * ?")
    // 每周一上午10点
     @Scheduled(cron = "0 0 10 ? * 1")
    // 测试，每分钟
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void send() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            QueryWrapper<DailyPlay> dailyPlayQueryWrapper = new QueryWrapper<>();
            dailyPlayQueryWrapper.eq("sid", user.getSid());
            List<DailyPlay> dailyPlayList = dailyPlayMapper.selectList(dailyPlayQueryWrapper);
            for (DailyPlay dailyPlay : dailyPlayList) {
                dailyPlay.setPlayCount(dailyPlay.getNewPlayCount());
                dailyPlayMapper.updateById(dailyPlay);
            }
            msgClient.sendMsg(user.getSid(), 0, 2);
        }
    }

    @Scheduled(cron = "0 0 23 * * ?")
    // 测试，每分钟
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void storeData() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int dayNumber = dayOfWeek.getValue();
        System.out.println("Today is: " + dayNumber); // 输出今天是周几的数字表示

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            QueryWrapper<DailyPlay> dailyPlayQueryWrapper = new QueryWrapper<>();
            dailyPlayQueryWrapper.eq("sid", user.getSid()).eq("weekday", dayNumber);
            DailyPlay dailyPlay = dailyPlayMapper.selectOne(dailyPlayQueryWrapper);
            if (dailyPlay == null) {
                initData(user.getSid());
            }

            int lastDay = dayNumber - 1;
            if (lastDay == 0) lastDay = 7;
            QueryWrapper<DailyPlay> dailyPlayQueryWrapper1 = new QueryWrapper<>();
            dailyPlayQueryWrapper1.eq("sid", user.getSid()).eq("weekday", lastDay);
            DailyPlay lastDailyPlay = dailyPlayMapper.selectOne(dailyPlayQueryWrapper1);

            UpdateWrapper<DailyPlay> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", user.getSid()).eq("weekday", dayNumber)
                    .set("user_play_count", user.getPlayCount())
                    .set("new_play_count", user.getPlayCount() - lastDailyPlay.getUserPlayCount());

            dailyPlayMapper.update(null, updateWrapper);
        }
    }


    @Override
    public void initData(String sid) {
        for (int i = 1; i <= 7; i++) {
            dailyPlayMapper.insert(
                    new DailyPlay(
                            null,
                            sid,
                            i,
                            0,
                            0,
                            0,
                            0,
                            0,
                            new Date()
                    )
            );
        }
    }


    @Override
    public List<DailyPlay> getDailyData(String sid) {
        QueryWrapper<DailyPlay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        return dailyPlayMapper.selectList(queryWrapper);
    }

}
