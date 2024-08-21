package com.bilimili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bilimili.video.clients.UserClient;
import com.bilimili.video.dto.VideoDTO;
import com.bilimili.video.mapper.UserVideoMapper;
import com.bilimili.video.response.CustomResponse;
import com.bilimili.video.service.UserVideoService;
import com.bilimili.video.service.VideoService;
import com.bilimili.video.dao.UserVideo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UserVideoServiceImpl implements UserVideoService {
    @Autowired
    private UserVideoMapper userVideoMapper;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserClient userClient;

    /**
     * 更新播放次数以及最近播放时间，顺便返回记录信息，没有记录则创建新记录
     * @param sid   用户ID
     * @param vid   视频ID
     * @return 更新后的数据信息
     */
    @Override
    public UserVideo updatePlay(String sid, Integer vid) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("vid", vid);
        UserVideo userVideo = userVideoMapper.selectOne(queryWrapper);
        VideoDTO video = videoService.getVideoWithDataByVid(vid);
        if (userVideo == null) {
            // 记录不存在，创建新记录
            userVideo = new UserVideo(null, sid, vid, 1, 0, 0, 0, 0, new Date(), null, null);
            userVideoMapper.insert(userVideo);

            videoService.updateStats(vid, "play", true, 1);
            userClient.updateStats(video.getSid(), "play_count", true, 1);

        } else if (System.currentTimeMillis() - userVideo.getPlayTime().getTime() <= 30000) {
            // 如果最近30秒内播放过则不更新记录，直接返回
            return userVideo;
        } else {
            userVideo.setPlay(userVideo.getPlay() + 1);
            userVideo.setPlayTime(new Date());
            userVideoMapper.updateById(userVideo);

            videoService.updateStats(vid, "play", true, 1);
            userClient.updateStats(video.getSid(), "play_count", true, 1);
        }
        // 异步线程更新video表和redis
//        CompletableFuture.runAsync(() -> {
//            videoService.updateStats(vid, "play", true, 1);
//        }, taskExecutor);
        return userVideo;
    }

    /**
     * 点赞或点踩，返回更新后的信息
     * @param sid   用户ID
     * @param vid   视频ID
     * @param isLove    赞还是踩 true赞 false踩
     * @param isSet     设置还是取消  true设置 false取消
     * @return  更新后的信息
     */
    @Override
    public UserVideo setLoveOrUnlove(String sid, Integer vid, boolean isLove, boolean isSet) {
        VideoDTO video = videoService.getVideoWithDataByVid(vid);
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("vid", vid);
        UserVideo userVideo = userVideoMapper.selectOne(queryWrapper);
        if (isLove && isSet) {
            // 点赞
            if (userVideo.getLove() == 1) {
                // 原本就点了赞就直接返回
                return userVideo;
            }
            // 插入点赞记录
            userVideo.setLove(1);
            UpdateWrapper<UserVideo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", sid).eq("vid", vid);
            updateWrapper.setSql("love = 1");
            updateWrapper.set("love_time", new Date());

            // 原本没点踩，只需要点赞就行
            videoService.updateStats(vid, "good", true, 1);
            userClient.updateStats(video.getSid(), "love_count", true, 1);

            userVideoMapper.update(null, updateWrapper);
            // 通知up主视频被赞了
//            CompletableFuture.runAsync(() -> {
//                // 查询UP主uid
//                Video video = videoMapper.selectById(vid);
//                if(!Objects.equals(video.getUid(), uid)) {
//                    // 更新最新被点赞的视频
//                    redisUtil.zset("be_loved_zset:" + video.getUid(), vid);
//                    msgUnreadService.addOneUnread(video.getUid(), "love");
//                    // netty 通知未读消息
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("type", "接收");
//                    Set<Channel> channels = IMServer.userChannel.get(video.getUid());
//                    if (channels != null) {
//                        for (Channel channel: channels) {
//                            channel.writeAndFlush(IMResponse.message("love", map));
//                        }
//                    }
//                }
//            }, taskExecutor);
        } else if (isLove) {
            // 取消点赞
            if (userVideo.getLove() == 0) {
                // 原本就没有点赞就直接返回
                return userVideo;
            }
            // 取消赞
            userVideo.setLove(0);
            UpdateWrapper<UserVideo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", sid).eq("vid", vid);
            updateWrapper.setSql("love = 0");
            userVideoMapper.update(null, updateWrapper);


            videoService.updateStats(vid, "good", false, 1);
            userClient.updateStats(video.getSid(), "love_count", false, 1);
        } else if (isSet) {
            // 点踩
            if (userVideo.getUnlove() == 1) {
                // 原本就点了踩就直接返回
                return userVideo;
            }
            // 更新用户点踩记录
            userVideo.setUnlove(1);
            UpdateWrapper<UserVideo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", sid).eq("vid", vid);
            updateWrapper.setSql("unlove = 1");
            if (userVideo.getLove() == 1) {
                // 原本点了赞，要取消赞
                userVideo.setLove(0);
                updateWrapper.setSql("love = 0");

                videoService.updateGoodAndBad(vid, false);
            } else {
                // 原本没点赞，只需要点踩就行

                videoService.updateStats(vid, "bad", true, 1);

            }
            userVideoMapper.update(null, updateWrapper);
        } else {
            // 取消点踩
            if (userVideo.getUnlove() == 0) {
                // 原本就没有点踩就直接返回
                return userVideo;
            }
            // 取消用户点踩记录
            userVideo.setUnlove(0);
            UpdateWrapper<UserVideo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sid", sid).eq("vid", vid);
            updateWrapper.setSql("unlove = 0");
            userVideoMapper.update(null, updateWrapper);

            videoService.updateStats(vid, "bad", false, 1);

        }
        return userVideo;
    }

    /**
     * Description: 获取sid看过的视频
     *
     * @param sid
     * @return java.util.List<java.lang.Integer>
     * @author Yury
     */
    @Override
    public List<Integer> getIdsBySid(String sid, Integer quantity) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).orderByDesc("play_time").last("LIMIT " + quantity);
        List<UserVideo> userVideoList = userVideoMapper.selectList(queryWrapper);
        List<Integer> ids = new ArrayList<>();
        for (UserVideo userVideo : userVideoList) {
            ids.add(userVideo.getVid());
        }
        return ids;
    }

    @Override
    public Boolean isCollected(String sid, Integer vid) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("vid", vid);
        UserVideo userVideo = userVideoMapper.selectOne(queryWrapper);
        return userVideo.getCollect() != 0;
    }

    @Override
    public CustomResponse addOrCancelFavoriteVideo(String sid, Integer vid) {
        CustomResponse customResponse = new CustomResponse();
        UpdateWrapper<UserVideo> updateWrapper = new UpdateWrapper<>();

        if (isCollected(sid, vid)) {
            updateWrapper.eq("sid", sid).eq("vid", vid).set("collect", 0);
            userVideoMapper.update(null, updateWrapper);

            // UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            // userUpdateWrapper.eq("sid", sid).setSql("collect_video_count = collect_video_count - 1");

            videoService.updateStats(vid, "collect", false, 1);
            userClient.updateStats(sid, "collect_video_count", false, 1);

            // userMapper.update(null, userUpdateWrapper);
            customResponse.setMessage("取消收藏");
        } else {
            updateWrapper.eq("sid", sid).eq("vid", vid).set("collect", 1);
            userVideoMapper.update(null, updateWrapper);

            // UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            // userUpdateWrapper.eq("sid", sid).setSql("collect_video_count = collect_video_count + 1");

            videoService.updateStats(vid, "collect", true, 1);
            userClient.updateStats(sid, "collect_video_count", true, 1);

            // userMapper.update(null, userUpdateWrapper);
            customResponse.setMessage("添加收藏");
        }
        return customResponse;
    }

    @Override
    public List<Integer> getUserCollectVideoList(String sid) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("collect", 1).orderByDesc("collect_time");
        List<UserVideo> userVideoList = userVideoMapper.selectList(queryWrapper);
        List<Integer> ids = new ArrayList<>();
        for (UserVideo userVideo : userVideoList) {
            ids.add(userVideo.getVid());
        }
        return ids;
    }

    @Override
    public Boolean isLoved(String sid, Integer vid) {
        QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid", sid).eq("vid", vid);
        UserVideo userVideo = userVideoMapper.selectOne(queryWrapper);
        return userVideo.getLove() != 0;
    }

    @Override
    public CustomResponse deleteVideosFromFavorite(String sid, List<Integer> vidList) {
        CustomResponse customResponse = new CustomResponse();
        for(Integer vid : vidList) {
            QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("vid", vid).eq("sid", sid);
            if (isCollected(sid, vid)) {
                addOrCancelFavoriteVideo(sid, vid);
            }
        }
        customResponse.setMessage("取消收藏成功");

        return customResponse;
    }
}
