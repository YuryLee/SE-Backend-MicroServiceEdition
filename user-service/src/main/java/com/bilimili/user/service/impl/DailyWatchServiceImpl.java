package com.bilimili.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.bilimili.user.clients.MsgClient;
import com.bilimili.user.clients.VideoClient;
import com.bilimili.user.dao.DailyWatch;
import com.bilimili.user.dao.User;
import com.bilimili.user.dao.UserVideo;
import com.bilimili.user.mapper.DailyWatchMapper;
import com.bilimili.user.mapper.UserMapper;
import com.bilimili.user.service.DailyWatchService;
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
public class DailyWatchServiceImpl implements DailyWatchService {

    @Autowired
    private MsgClient msgClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DailyWatchMapper dailyWatchMapper;

    @Autowired
    private VideoClient videoClient;

    // 每天午夜12点执行
    //@Scheduled(cron = "0 0 0 * * ?")
    //每周一上午10点
     @Scheduled(cron = "0 0 10 ? * 1")
    // 测试，每分钟
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void send() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            String sid = user.getSid();
            int loveCount = 0, collectCount = 0, playCount = 0;
            QueryWrapper<UserVideo> userVideoQueryWrapper = new QueryWrapper<>();
            userVideoQueryWrapper.eq("sid", sid);
            List<UserVideo> userVideoList = videoClient.getUserVideoList(sid);
            for (UserVideo userVideo : userVideoList) {
                playCount += userVideo.getPlay();
                loveCount += userVideo.getLove();
                collectCount += userVideo.getCollect();
            }

            UpdateWrapper<DailyWatch> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("sid", user.getSid()).eq("weekday", 8)
                    .set("love_count", loveCount)
                    .set("collect_count", collectCount)
                    .set("play_count", playCount);
            dailyWatchMapper.update(null, updateWrapper1);


            QueryWrapper<DailyWatch> dailyWatchQueryWrapper = new QueryWrapper<>();
            dailyWatchQueryWrapper.eq("sid", user.getSid());
            List<DailyWatch> dailyWatchList = dailyWatchMapper.selectList(dailyWatchQueryWrapper);
            for (DailyWatch dailyWatch : dailyWatchList) {
                dailyWatch.setPlayCount(dailyWatch.getNewPlayCount());
                dailyWatchMapper.updateById(dailyWatch);
            }
            msgClient.sendMsg(user.getSid(), 0, 3);
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
            String sid = user.getSid();

            QueryWrapper<DailyWatch> dailyWatchQueryWrapper = new QueryWrapper<>();
            dailyWatchQueryWrapper.eq("sid", user.getSid()).eq("weekday", dayNumber);
            DailyWatch dailyWatch = dailyWatchMapper.selectOne(dailyWatchQueryWrapper);
            if (dailyWatch == null) {
                initData(user.getSid());
            }

            int lastDay = dayNumber - 1;
            if (lastDay == 0) lastDay = 7;
            QueryWrapper<DailyWatch> dailyPlayQueryWrapper1 = new QueryWrapper<>();
            dailyPlayQueryWrapper1.eq("sid", sid).eq("weekday", lastDay);
            DailyWatch lastDailyPlay = dailyWatchMapper.selectOne(dailyPlayQueryWrapper1);

            int loveCount = 0, collectCount = 0, playCount = 0;
            QueryWrapper<UserVideo> userVideoQueryWrapper = new QueryWrapper<>();
            userVideoQueryWrapper.eq("sid", sid);
            List<UserVideo> userVideoList = videoClient.getUserVideoList(sid);
            for (UserVideo userVideo : userVideoList) {
                if (userVideo.getPlay() != 0) {
                    playCount += userVideo.getPlay();
                } else if (userVideo.getLove() != 0) {
                    loveCount++;
                } else if (userVideo.getCollect() != 0) {
                    collectCount++;
                }
            }

//            UpdateWrapper<DailyWatch> updateWrapper1 = new UpdateWrapper<>();
//            updateWrapper1.eq("sid", user.getSid()).eq("weekday", 8)
//                    .set("love_count", loveCount)
//                    .set("collect_count", collectCount)
//                    .set("play_count", playCount);
//
//            dailyWatchMapper.update(null, updateWrapper1);


            UpdateWrapper<DailyWatch> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", user.getSid()).eq("weekday", dayNumber)
                    .set("user_play_count", playCount)
                    .set("new_play_count", playCount - lastDailyPlay.getUserPlayCount());

            dailyWatchMapper.update(null, updateWrapper);
        }
    }


    @Override
    public void initData(String sid) {
        for (int i = 1; i <= 8; i++) {
            dailyWatchMapper.insert(
                    new DailyWatch(
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
    public List<DailyWatch> getDailyData(String sid) {
        QueryWrapper<DailyWatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid);
        return dailyWatchMapper.selectList(queryWrapper);
    }

}
